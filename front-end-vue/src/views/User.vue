<template>
  <div>


    <div style="padding: 10px 0">
      <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search"
                v-model="username"></el-input>
      <el-input style="width: 200px" placeholder="请输入邮箱" suffix-icon="el-icon-search"
                class="ml-5" v-model="email"></el-input>
      <el-input style="width: 200px" placeholder="请输入地址" suffix-icon="el-icon-search"
                class="ml-5" v-model="address"></el-input>
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
      <el-button type="primary" class="ml-5">导入 <i class="el-icon-bottom"></i></el-button>
      <el-button type="primary">导出 <i class="el-icon-top"></i></el-button>

    </div>

    <el-table :data="tableData" border stripe @selection-change="handleSelectionChange">
      <el-table-column
          type="selection"
          width="55">
      </el-table-column>
      <el-table-column prop="id" label="ID" width="80">
      </el-table-column>
      <el-table-column prop="username" label="用户名" width="100">
      </el-table-column>
      <el-table-column prop="nickname" label="昵称" width="120">
      </el-table-column>
      <el-table-column prop="address" label="地址">
      </el-table-column>
      <el-table-column prop="email" label="邮箱">
      </el-table-column>
      <el-table-column prop="phone" label="电话">
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
    <el-dialog title="新增" :visible.sync="dialogFormVisible" width="30%">
      <el-form :model="form" label-width="80px">
        <el-form-item label="用户名">
          <el-input v-model="form.username" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="昵称">
          <el-input v-model="form.nickname" autocomplete="off"></el-input>
          <!--                <el-select v-model="form.nickname" placeholder="请选择活动区域">-->
          <!--                  <el-option label="区域一" value="shanghai"></el-option>-->
          <!--                  <el-option label="区域二" value="beijing"></el-option>-->
          <!--                </el-select>-->
        </el-form-item>
        <el-form-item label="地址">
          <el-input v-model="form.address" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="电话">
          <el-input v-model="form.phone" autocomplete="off"></el-input>
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
      pageSize: 10,
      username: "",
      email: "",
      address: "",
      dialogFormVisible: false,  // 对话框
      form: {},
      multipleSelection: [],}
  },
  created() {
    this.load()
  },
  methods: {
    load(pageNum = this.pageNum) {
      this.request.get("/user/page", {
        params: {
          pageNum: pageNum,
          pageSize: this.pageSize,
          username: this.username,
          email: this.email,
          address: this.address
        }
      }).then(res => {
        console.log(res)

        this.tableData = res.data.records
        this.total = res.data.total

      })
      // fetch("http://localhost:8080/user/page?pageNum=" + this.pageNum + "&pageSize=" + this.pageSize + "&username=" + this.username)
      //     .then(res => res.json()).then(res => {
      //   console.log(res)
      //   this.tableData = res.data
      //   this.total = res.total
      // })
    },
    fuzzySearch() {
      this.request.get("/user/search", {
        params: {
          pageNum: 1,
          pageSize: this.pageSize,
          username: this.username,
          email: this.email,
          address: this.address
        }
      }).then(res => {
        console.log(res.data)
        this.pageNum = 1
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    save() {
      this.request.post("/user", this.form).then(res => {
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
      this.request.delete("/user/" + id).then(res => {
        if (res.data) {
          this.$message.success("Delete success")
          this.load()
        } else {
          this.$message.error("Error")
        }
      })
    },
    reset() {
      this.username = ""
      this.email = ""
      this.address = ""
      this.pageNum = 1
      this.load()
    },
    handleSelectionChange(val) {
      console.log(val)
      this.multipleSelection = val
    },
    delBatch() {
      let ids = this.multipleSelection.map(v => v.id)  //[{}, {}, {}] => [1, 2, 3]
      this.request.post("/user/del/batch", ids).then(res => {
        if (res.data) {
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
    handleAdd() {
      this.dialogFormVisible = true
      this.form = {}
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogFormVisible = true
    }
  }
}
</script>

<style scoped>
.el-table th {
  background-color: #eeeeee !important;
  color: black;
  font-weight: bold;
}
</style>