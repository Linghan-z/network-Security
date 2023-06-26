<template>
  <div>
    <div style="padding: 10px 0">
      <el-input class="mr-5" style="width: 200px" placeholder="请输入头实体名称" suffix-icon="el-icon-search"
                v-model="headValue"></el-input>
      <el-select class="mr-5" v-model="relation" filterable clearable placeholder="请选择">
        <el-option
            v-for="item in relations"
            :key="item.value"
            :value="item.value">
        </el-option>
      </el-select>
      <el-input class="mr-5" style="width: 200px" placeholder="请输入尾实体名称" suffix-icon="el-icon-search"
                v-model="tailValue"></el-input>
      <el-button class="ml-5" type="primary" @click="fuzzySearch">搜索</el-button>
      <el-button class="ml-5" type="warning" @click="reset">重置</el-button>
    </div>
    <div style="margin: 10px 0">
      <el-button type="primary" @click="handleAdd">新增 <i class="el-icon-circle-plus-outline"></i>
      </el-button>
      <el-popconfirm
          class="ml-5"
          confirm-button-text='好的'
          cancel-button-text='不用了'
          icon="el-icon-info"
          icon-color="red"
          title="确定批量删除删除这些数据吗？"
          @confirm="delBatch"
      >
        <el-button type="danger" slot="reference">批量删除 <i class="el-icon-remove-outline"></i></el-button>
      </el-popconfirm>

    </div>
    <el-table :data="tableData" border stripe @selection-change="handleSelectionChange">
      <el-table-column
          type="selection"
          width="55">
      </el-table-column>
      <el-table-column prop="id" label="id">
      </el-table-column>
      <el-table-column prop="headId" label="头实体id">
      </el-table-column>
      <el-table-column prop="headValue" label="头实体">
      </el-table-column>
      <el-table-column prop="relation" label="关系" min-width="120px">
      </el-table-column>
      <el-table-column prop="tailId" label="尾实体id">
      </el-table-column>
      <el-table-column prop="tailValue" label="尾实体">
      </el-table-column>
      <el-table-column>
        <template slot-scope="scope" label="操作">
          <el-popconfirm
              class="ml-5"
              confirm-button-text='好的'
              cancel-button-text='不用了'
              icon="el-icon-info"
              icon-color="red"
              title="确定删除吗？"
              @confirm="del(scope.row.id)"
          >
            <el-button type="danger" slot="reference"><i class="el-icon-remove-outline"></i></el-button>
          </el-popconfirm>

        </template>
      </el-table-column>
    </el-table>

    <div style="padding: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[2, 5, 10, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>

    <!-- Form -->
    <el-dialog title="新增" :visible.sync="dialogFormVisible" width="35%"
               class="flex column justify-center align-center">
      <el-dialog
          width="30%"
          title="建立新关系"
          :visible.sync="innerDialogVisible"
          append-to-body>
        <div>
          <el-row>
            <el-col :span="4" style="font-size: 16px; color: darkblue">
              <div>头实体：</div>
            </el-col>
            <el-col :span="5">
              <div>{{ form.headId }}</div>
            </el-col>
            <el-col :span="15">
              <div>{{ form.headValue }}</div>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="4" style="font-size: 16px; color: darkblue">
              <div>尾实体：</div>
            </el-col>
            <el-col :span="5">
              <div>{{ form.tailId }}</div>
            </el-col>
            <el-col :span="15">
              <div>{{ form.tailValue }}</div>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="4" style="font-size: 16px; color: darkblue">
              <div>关系：</div>
            </el-col>
            <el-col :span="20">
              <div>{{ triple }}</div>
            </el-col>
          </el-row>
        </div>
        <div slot="footer" class="dialog-footer">
          <el-button @click="innerDialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="save">建立关系</el-button>
        </div>
      </el-dialog>
      <el-form label-width="80px">
        <el-form-item label="头实体id">
          <el-input v-model="form.headId" autocomplete="off" disabled></el-input>
        </el-form-item>
        <el-form-item label="头实体值">
          <el-input v-model="form.headValue" autocomplete="off"
                    @change="handleHeadValueChange(form.headValue)"></el-input>
        </el-form-item>
        <el-form-item label="尾实体id">
          <el-input v-model="form.tailId" autocomplete="off" disabled></el-input>
        </el-form-item>
        <el-form-item label="尾实体值">
          <el-input v-model="form.tailValue" autocomplete="off"
                    @change="handleTailValueChange(form.tailValue)"></el-input>
        </el-form-item>
        <el-form-item label="关系">
          <el-input v-model="form.relation" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleInnerDialog">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "File",
  data() {
    return {
      tableData: [],
      name: '',
      multipleSelection: [],
      relations: [],
      dialogFormVisible: false,  // 对话框
      form: {},
      pageNum: 1,
      pageSize: 20,
      total: 0,
      headValue: "",
      relation: "",
      tailValue: "",
      innerDialogVisible: false,
      triple: ''
    }
  },
  created() {
    this.load()
  },
  methods: {
    getRelations() {
      this.request.get("/triples/relations").then(res => {
        this.relations = res.data
      })
    },
    load() {
      this.getRelations()
      this.request.get("/triples/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
        }
      }).then(res => {
        console.log(res)
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    fuzzySearch() {
      this.request.get("/triples/fuzzySearch", {
        params: {
          pageNum: 1,
          pageSize: this.pageSize,
          headValue: this.headValue,
          relation: this.relation,
          tailValue: this.tailValue
        }
      }).then(res => {
        console.log(res.data)
        this.pageNum = 1
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    del(id) {
      this.request.post("/triples/executeCypher/delete/" + id).then(res => {
        console.log(res)
        if (res.code === '200') {
          this.$message.success("Delete success")
          this.load()
        } else {
          this.$message.error("Error")
        }
      })
    },
    handleAdd() {
      this.dialogFormVisible = true
      this.form = {}
    },
    reset() {
      this.name = ""
      this.pageNum = 1
      this.load()
    },
    handleHeadValueChange(headvalue) {
      this.request.get("/entityInfo/searchId/" + headvalue).then(res => {
        // console.log(res)
        this.form.headId = res.data
      })
    },
    handleTailValueChange(tailvalue) {
      this.request.get("/entityInfo/searchId/" + tailvalue).then(res => {
        // console.log(res)
        this.form.tailId = res.data
      })
    },
    save() {
      this.request.post("/triples/save", this.form).then(res => {
        if (res.data) {
          this.$message.success("Success")
          this.dialogFormVisible = false
          let pageNumber = Math.floor(this.total / this.pageSize) + 1
          this.load(pageNumber)
        } else {
          this.$message.error("Error")
        }
      })
    },
    handleSelectionChange(val) {
      console.log(val)
      this.multipleSelection = val
    },
    delBatch() {
      let ids = this.multipleSelection.map(v => v.id)  //[{}, {}, {}] => [1, 2, 3]
      this.request.post("/triples/executeCypher/delete/batch", ids).then(res => {
        if (res.code === '200') {
          this.$message.success("Delete batch success")
          this.load()
        } else {
          this.$message.error("Error")
        }
      })
    },
    handleSizeChange(pageSize) {
      console.log(pageSize)
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      console.log(pageNum)
      this.pageNum = pageNum
      this.load()
    },
    handleInnerDialog() {
      this.innerDialogVisible = true
      this.triple = '(' + this.form.headValue + ')' + '—' + '[' + this.form.relation + ']' + '—>' + '(' + this.form.tailValue + ')'
    }
  }
}
</script>

<style scoped lang="scss">
.el-dialog .el-row {
  font-size: 14px;
  height: 25px;
  line-height: 25px;
}
</style>