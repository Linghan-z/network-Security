<template>
  <div>
    <meta charset="UTF-8"/>
    <el-select v-model="organizations" multiple clearable placeholder="请选择">
      <el-option
          v-for="item in options"
          :key="item.value"
          :value="item.value">
      </el-option>
    </el-select>
    <el-button class="ml-5" type="primary" @click="search">搜索</el-button>
    <EChartsDisplay
        ref="eChartDisplay"></EChartsDisplay>
  </div>
</template>

<script>
import EChartsDisplay from "@/components/EChartsDisplay.vue";
  export default {
    name: 'Neo4jOrganizationDisplay',
    components: {
      EChartsDisplay
    },
    data() {
      return {
        options: [],
        organizations: [],
      }
    },
    created() {
      this.getOrganizations()
    },
    methods: {
      getOrganizations() {
        this.request.get("/entityInfo/organizations").then(res => {
          this.options = res.data
        })
      },
      search() {
        const params = new URLSearchParams();
        for (let i = 0; i < this.organizations.length; i++) {
          params.append('organizationNameList', this.organizations[i]);
        }
        this.$refs.eChartDisplay.drawECharts('http://localhost:8080//triples/echarts/organization?' + params.toString())
      },
    }
  }
</script>

<style scoped lang="scss">

</style>