<template>
  <div>
    <div class="flex row align-center justify-start">
      <div style="padding: 10px 0">
        <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search"
                  v-model="entityValue"></el-input>
        <el-button class="ml-5" type="primary" @click="fuzzySearch">搜索</el-button>
        <el-button class="ml-5" type="warning" @click="reset">重置</el-button>
      </div>
      <div style="margin-left:20px; padding: 10px 0" class="flex row justify-start align-center">
        <div class="ml-5 mr-5" style="font-size: 15px">选择类别查看</div>
        <el-select v-model="label" @change = handleLabelChange placeholder="请选择">
          <el-option
              v-for="item in labelOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value">
          </el-option>
        </el-select>
      </div>
      <div style="margin-left: 20px">

        <el-button class="ml-5" type="primary" @click="searchNotUpdatedEntity">查找未同步实体</el-button>
        <el-popconfirm
            class="ml-5"
            confirm-button-text='好的'
            cancel-button-text='不用了'
            icon="el-icon-info"
            icon-color="blue"
            title="确定批量同步这些数据到neo4j吗？"
            @confirm="syncToNeo4j"
        >
        <el-button class="ml-5" type="success" slot="reference">一键同步</el-button>
        </el-popconfirm>
      </div>
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
      <el-table-column type="expand" >
        <template slot-scope="props">
          <el-form label-position="left" inline class="table-expand">
            <el-form-item label="节点Id">
              <div>{{ props.row.id }}</div>
            </el-form-item>
            <el-form-item label="节点Value">
              <div>{{ props.row.value }}</div>
            </el-form-item>
            <el-form-item label="介绍"  v-show="handleShowIntroduction">
              <div>{{ props.row.introduction }}</div>
            </el-form-item>
            <el-form-item label="创建时间" v-show="isOrganization">
              <div>{{ props.row.occurtime }}</div>
            </el-form-item>
            <el-form-item label="创建动机" v-show="isOrganization">
              <div>{{ props.row.motivation }}</div>
            </el-form-item>
            <el-form-item label="格式" v-show="isSha256">
              <div>{{ props.row.format }}</div>
            </el-form-item>
          </el-form>
        </template>
      </el-table-column>
      <el-table-column prop="id" label="ID" min-width="80">
      </el-table-column>
      <el-table-column prop="value" label="Value" min-width="100">
      </el-table-column>
      <el-table-column prop="updated" label="Updated" min-width="20">
        <template slot-scope="scope">
          <div>{{scope.row.updated|isUpdated}}</div>
        </template>
      </el-table-column>
      <el-table-column>
        <template slot-scope="scope" label="操作">
          <el-button type="primary" @click="handleEdit(scope.row)"><i class="el-icon-edit"></i></el-button>
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
    <el-dialog title="新增" :visible.sync="dialogFormVisible" width="30%" class="flex column justify-center, align-start">
      <div style="margin-bottom: 20px">
        <span style="font-size: 14px; margin-right: 15px; margin-left: 10px">选择类别</span>
        <el-select v-model="label" @change = handleLabelChange placeholder="请选择">
          <el-option
              v-for="item in labelOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value">
          </el-option>
        </el-select>
      </div>
      <el-form label-width="80px">
        <el-form-item label="节点类型">
          <el-input v-model="form.label" autocomplete="off" disabled></el-input>
        </el-form-item>
        <el-form-item label="节点值">
          <el-input v-model="form.value" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="介绍"  v-show="handleShowIntroduction">
          <el-input type="textarea" :autosize="{ minRows: 2, maxRows: 20}" v-model="form.introduction" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="创建时间" v-show="isOrganization">
          <el-input v-model="form.occurtime" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="创建动机" v-show="isOrganization">
          <el-input v-model="form.motivation" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="格式" v-show="isSha256">
          <el-input v-model="form.format" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
export default {
  name: "User",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 20,
      entityValue: "",
      label: "",
      dialogFormVisible: false,  // 对话框
      form: {},
      multipleSelection: [],
      labelOptions:
          [
          {
        value: 'Organization',
        label: '组织'
      }, {
        value: 'Area',
        label: '地区'
      }, {
        value: 'Attacktype',
        label: '攻击方式'
      }, {
        value: 'Industry',
        label: '行业'
      }, {
        value: 'Ip',
        label: 'IP'
      }, {
        value: 'Sha256',
        label: 'Sha256'
      }, {
        value: 'Domain',
        label: 'Domain'
      }],
      handleShowIntroduction:false,
      isOrganization:false,
      isSha256:false,
      newLabel:"",
      handleShowIntroductionInDialog:false,
      isOrganizationInDialog:false,
      isSha256InDialog:false,
    }
  },
  filters: {
    // 通过自定义过滤器
    isUpdated(updated) {
      console.log(updated)
      if (updated) return 'Y'
      return 'N'
    }
  },
  created() {
    this.load()
  },
  methods: {
    load(pageNum = this.pageNum) {
      this.request.get("/entityInfo/page", {
        params: {
          pageNum: pageNum,
          pageSize: this.pageSize,
          label: this.label,
        }
      }).then(res => {
        console.log(res)
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    fuzzySearch() {
      this.request.get("/entityInfo/search", {
        params: {
          pageNum: 1,
          pageSize: this.pageSize,
          entityValue: this.entityValue,
        }
      }).then(res => {
        console.log(res.data)
        this.pageNum = 1
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    save() {
      this.request.post("/entityInfo/save", this.form).then(res => {
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
    del(id) {
      this.request.post("/entityInfo/executeCypher/delete/" + id).then(res => {
        if (res.code === "200") {
          this.$message.success("Delete success")
          this.load()
        } else {
          this.$message.error("Error")
        }
      })
    },
    reset() {
      this.username = ""
      this.pageNum = 1
      this.load()
    },
    handleSelectionChange(val) {
      console.log(val)
      this.multipleSelection = val
    },
    delBatch() {
      let ids = this.multipleSelection.map(v => v.id)  //[{}, {}, {}] => [1, 2, 3]
      this.request.post("/entityInfo/executeCypher/delete/batch", ids).then(res => {
        if (res.code === "200") {
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
    handleLabelChange() {
      this.form.label = this.label
      if (this.label === "Organization"){
        this.isOrganization = true
        this.handleShowIntroduction = true
        this.isSha256 = false
      } else if (this.label === "Attacktype") {
        this.isOrganization = false
        this.isSha256 = false
        this.handleShowIntroduction = true
      } else if (this.label === "Sha256") {
        this.isOrganization = false
        this.handleShowIntroduction = false
        this.isSha256 = true
      } else {
        this.isOrganization = false
        this.handleShowIntroduction = false
        this.isSha256 = false
      }
      this.load()
    },
    handleCurrentChange(pageNum) {
      console.log(pageNum)
      this.pageNum = pageNum
      this.load()
    },
    handleAdd() {
      this.dialogFormVisible = true
      this.form = {}
      this.form.label = this.label
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogFormVisible = true
    },
    syncToNeo4j() {
      let ids = this.multipleSelection.map(v => v.id)  //[{}, {}, {}] => [1, 2, 3]
      this.request.post("/entityInfo/executeCypher/setNodeInfo", ids).then(res => {
        console.log(res)
        if (res.code === "200") {
          this.$message.success("update batch success")
          this.load()
        } else {
          this.$message.error("Error")
        }
      })
    },
    searchNotUpdatedEntity() {
      this.request.get("/entityInfo/notUpdatedPage",{
        params: {
          pageNum: 1,
          pageSize: this.pageSize,
        }
      }).then(res => {
        console.log(res)
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
  }
}
</script>

<style lang="scss">
.el-table th {
  background-color: #eeeeee !important;
  color: black;
  font-weight: bold;
}
.table-expand {
  font-size: 0;
}
.table-expand label {
  width: 90px;
  color: #99a9bf;
}
.table-expand .el-form-item {
  margin-right: 0;
  margin-bottom: 0;
  width: 100%;
}
.table-expand .el-form-item div{
  width: calc(100% - 90px);
}
</style>