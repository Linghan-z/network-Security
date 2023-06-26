<template>
  <div>
    <meta charset="UTF-8"/>
    <div id="main" style="width: 80vw; height: 700px"></div>
  </div>


</template>

<script>
import * as echarts from 'echarts'
import $ from 'jquery'

export default {
  name: "EchartsDisplay",
  props: {
    url: String,
  },
  data() {
    return {
      options: [],
    }
  },
  methods: {
    drawECharts(url) {
      this.generateUrl(url)
      this.draw()
    },
    generateUrl(url) {
      this.url = url
    },
    draw() {
      let chartDom = document.getElementById('main');
      let myChart = echarts.init(chartDom);
      let option;
      const params = new URLSearchParams();
      myChart.showLoading();
      $.getJSON(this.url, function (graph) {
        console.log(graph)
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
              zoom: 2,
              draggable: true,
              edgeSymbol: ['circle', 'arrow'],
              edgeSymbolSize: [2, 5],
              force: {
                edgeLength: 30,
                layoutAnimation: true,
                repulsion: 100,
                gravity: 0.2,
                friction: 0.1
              },
              nodeScaleRatio: 0.8,
              label: {
                show: true,
                position: 'inside',
                formatter: '{b}'
              },
              labelLayout: {
                hideOverlap: true
              },
              scaleLimit: {
                min: 0.5,
                max: 5
              },
              lineStyle: {
                color: 'source',
              },
              emphasis: {
                focus: 'adjacency',
                lineStyle: {
                  width: 5
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

<style lang="scss">

</style>