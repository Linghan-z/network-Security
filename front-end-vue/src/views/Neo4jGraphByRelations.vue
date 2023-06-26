<template>
  <div class="myDiv">
    <meta charset="UTF-8"/>
    <el-select v-model="relations" multiple clearable placeholder="请选择">
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
  name: "Neo4jGraphByLabels",
  components: {
    EChartsDisplay
  },
  props: {},
  data() {
    return {
      options: [],
      values: [],
      relations: [],
    }
  },
  created() {
    this.getRelations()
    this.search()
  },
  methods: {
    getRelations() {
      this.request.get("/triples/relations").then(res => {
        this.options = res.data
      })
    },
    search() {
      const params = new URLSearchParams();
      for (let i = 0; i < this.relations.length; i++) {
        params.append('relationsList', this.relations[i]);
      }
      this.$refs.eChartDisplay.drawECharts('http://localhost:8080/triples/echarts/relations?' + params.toString())
    },
  },
}
</script>


<style lang="scss">

.myDiv {
  width: 85vw;
  height: 800px;
  margin: 0 auto;
}

textarea {
  border: 1px solid lightgray;
  margin: 5px;
  border-radius: 5px;
}

#viz {
  width: 100%;
  height: 80%;
  border: 1px solid #f1f3f4;
  font: 22pt arial;
}

input {
  border: 1px solid #ccc;
}

.search-row {
  min-height: 70px;
  border: 2px solid #d08120;
  padding: 20px;
  font-size: 17px;
  font-family: "Arial", "Microsoft YaHei", "黑体", "宋体", sans-serif;
}
</style>