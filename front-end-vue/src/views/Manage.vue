<template>
  <div style="height: 100%">
    <el-container style="height: 100%;">
      <el-aside :width="sideWith + 'px'" style="height: 100vh; box-shadow: 2px 0 6px rgb(0 21 41)">
        <Aside :isCollapse="isCollapse" :logoTextShow="logoTextShow"/>
      </el-aside>

      <el-container>
        <el-header style="border-bottom: 1px solid #ccc">
          <Header :collapseBtnClass="collapseBtnClass" :collapse="collapse" :user="user"/>
        </el-header>

        <el-main>
          <!--          当前页面的子路由会在router-view里面展示-->
          <router-view @refreshUser="getUser"/>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
// @ is an alias to /src

import Aside from "@/components/Aside.vue";
import Header from "@/components/Header.vue";

export default {
  name: 'HomeView',
  data() {
    return {
      collapseBtnClass: 'el-icon-s-fold',
      isCollapse: false,
      sideWith: 200,
      logoTextShow: true,
      user: {}
    }
  },
  components: {
    Aside,
    Header
  },
  created() {
    // 从后台获取最新的数据
    this.getUser()
  },
  methods: {
    collapse() { //点击收缩按钮触发
      this.isCollapse = !this.isCollapse
      if (this.isCollapse) {
        this.sideWith = 64
        this.logoTextShow = false
        this.collapseBtnClass = "el-icon-s-unfold"
      } else {
        this.sideWith = 200
        this.logoTextShow = true
        this.collapseBtnClass = "el-icon-s-fold"
      }
    },
    getUser() {
      let username = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")).username : ""
      // 从后台获取数据
      this.request.get("/user/username/" + username).then(res => {
        // 重新赋值后台的最新user数据
        this.user = res.data
      })
    }

  }
}
</script>

<style lang="scss">

</style>
