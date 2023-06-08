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
    <el-button class="ml-5" type="primary" @click="draw">搜索</el-button>
    <div id="main" style="width: 80vw; height: 700px"></div>
  </div>


</template>

<script>
import * as echarts from 'echarts'
import $ from 'jquery'

export default {
  name: "Echarts",
  data() {
    return {
      options: [],
      organizations: ["海莲花"],
    }
  },
  created() {
    this.getOrganizations()
  },
  mounted() {  // 页面元素渲染之后再触发
    this.draw()
  },
  methods: {
    getOrganizations() {
      this.request.get("/entityInfo/organizations").then(res => {
        this.options = res.data
      })
    },
    draw() {
      let chartDom = document.getElementById('main');
      let myChart = null
      myChart = echarts.init(chartDom);
      let option;
      const params = new URLSearchParams();
      for (let i = 0; i < this.organizations.length; i++) {
        params.append('organizationNameList', this.organizations[i]);
      }
      myChart.showLoading();
      $.getJSON('http://localhost:8080/triples/echarts?' + params.toString(), function (graph) {
        myChart.hideLoading();
        option = {
          tooltip: {},
          legend: [
            {
              data: graph.categories.map(function (a) {
                return a.name;
              })
            }
          ],
          animation: true,
          animationDuration: 1500,
          animationEasingUpdate: 'quinticInOut',
          animationEasing: 'cubicOut',
          series: [
            {
              type: 'graph',
              layout: 'force',
              data: graph.nodes,
              links: graph.links,
              categories: graph.categories,
              roam: true,
              zoom: 2.5,
              draggable: true,
              edgeSymbol: ['circle', 'arrow'],
              edgeSymbolSize: [2, 5],
              force: {
                edgeLength: 50
              },
              label: {
                show: true,
                position: 'inside',
                formatter: '{b}'
              },
              labelLayout: {
                hideOverlap: true
              },
              scaleLimit: {
                min: 1.5,
                max: 4
              },
              lineStyle: {
                color: 'source',
              },
              emphasis: {
                focus: 'adjacency',
                lineStyle: {
                  width: 10
                }
              }
            }
          ]
        };
        myChart.setOption(option);
      });
    }
  }
}
</script>

<style scoped>

</style>