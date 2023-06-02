<template>
  <div class="myDiv">
    <div>
      <span class="mr-5 ml-5">按类型查询</span>
      <el-cascader
          class="ml-5"
          :options="options"
          :props="{ checkStrictly: true }"
          clearable
          @change="generateCypher"></el-cascader>
      <el-input-number class="ml-5" v-model="limitNum" clearable placeholder="限制数量" style="width: 100px"
                       @blur="submit"></el-input-number>
      <el-button class="ml-5" type="primary" @click="submit">搜索</el-button>
      <el-button class="ml-5" type="text" @click="stabilize">Stabilize</el-button>
    </div>
    <div>
      <span class="ml-5" style="margin-right: 20px">实体名称</span>
      <el-input class="ml-5" v-model="searchValue" type="String" clearable placeholder="搜索内容" style="width: 150px"
                @change="valueSearch"></el-input>
      <el-button class="ml-5" type="primary" @click="submit">搜索</el-button>
    </div>
    <div id="viz"></div>
    Cypher query: <textarea rows="4" cols=50 id="cypher">{{ this.cypher }}</textarea><br>
  </div>
</template>

<script>
// 引入jquery.js
import $ from 'jquery';

// 引入neovis.js
import NeoVis from 'neovis.js'

export default {
  name: "Neo4jGraph",
  components: {},
  props: {},
  data() {
    return {
      viz: {},  //定义一个viz对象
      options:
          [
            {
              value: 'Organization',
              label: '组织',
              children: [{
                value: 'organization_has_area',
                label: '目标地区',
                children: [{
                  value: 'Area',
                  label: '地区'
                }]
              }, {
                value: 'organization_from',
                label: '发源地',
                children: [{
                  value: 'Area',
                  label: '地区'
                }]
              }, {
                value: 'organization_has_industry',
                label: '目标行业',
                children: [{
                  value: 'Industry',
                  label: '行业'
                }]
              }, {
                value: 'organization_has_attacktype',
                label: '攻击方式',
                children: [{
                  value: 'Attacktype',
                  label: '攻击方式'
                }]
              }, {
                value: 'organization_has_domain',
                label: '拥有域名',
                children: [{
                  value: 'Domain',
                  label: '域名'
                }]
              }, {
                value: 'organization_has_ip',
                label: 'IP',
                children: [{
                  value: 'Ip',
                  label: 'IP'
                }]
              }, {
                value: 'organization_has_sha256',
                label: 'Sha256',
                children: [{
                  value: 'Sha256',
                  label: 'Sha256'
                }]
              }]
            }, {
            value: 'Area',
            label: '地区',
            children: [{
              value: 'area_targeted_by_org',
              label: '被攻击',
              children: [{
                value: 'Organization',
                label: '组织'
              }]
            }, {
              value: 'area_has_organization',
              label: '发源地',
              children: [{
                value: 'Organization',
                label: '组织'
              }]
            }]
          }, {
            value: 'Industry',
            label: '行业',
            children: [{
              value: 'industry_targeted_by_org',
              label: '被攻击',
              children: [{
                value: 'Organization',
                label: '组织'
              }]
            }]
          }, {
            value: 'Attacktype',
            label: '攻击方式',
            children: [{
              value: 'attacktype_used_by_org',
              label: '被组织使用',
              children: [{
                value: 'Organization',
                label: '组织'
              }]
            }]
          }, {
            value: 'Ip',
            label: 'IP',
            children: [{
              value: 'ip_possessed_by_org',
              label: '所属组织',
              children: [{
                value: 'Organization',
                label: '组织'
              }]
            }]
          }, {
            value: 'Domain',
            label: 'Domain',
            children: [{
              value: 'domain_belongs_to_org',
              label: '所属组织',
              children: [{
                value: 'Organization',
                label: '组织'
              }]
            }]
          }, {
            value: 'Sha256',
            label: 'Sha256',
            children: [{
              value: 'sha256_possessed_by_org',
              label: '所属组织',
              children: [{
                value: 'Organization',
                label: '组织'
              }]
            }]
          }],
      values: [],
      cypher: '',
      from: '',
      relation: '',
      to: '',
      limitNum: 50,
      searchValue: '',
      searchValueCypher: '',
      limit: ''
    }
  },
  mounted() {
    this.draw();
  }, //渲染
  methods: {
    resetData() {
      this.searchValueCypher = ''
      this.searchLabel = ''
    },
    submit() {
      // var cypher = $("#cypher").val();
      this.valueSearch(this.searchValue)
      this.generateCypher(this.values, this.limitNum)
      this.viz.clearNetwork();
      if (this.cypher.length > 3) {
        this.viz.renderWithCypher(this.cypher);
      } else {
        console.log("reload");
        this.viz.reload();
      }
      // this.resetData()
    },
    stabilize() {
      this.viz.stabilize();
    },
    draw() {
      let config = {
        containerId: "viz",
        neo4j: {
          serverUrl: "bolt://localhost:7687",
          serverUser: "neo4j",
          serverPassword: "123456"
        },
        visConfig: {
          nodes: {},
          edges: {
            arrows: {
              to: {enabled: true}
            }
          },
        },
        labels: {
          Area: {
            label: "value",
          },
          Organization: {
            label: "value",
          },
          Industry: {
            label: "value",
          },
          Attacktype: {
            label: "value",
          },
          Ip: {
            label: "value",
          },
          Domain: {
            label: "value",
          },
          Sha256: {
            label: "value",
          }
        },
        relationships: {
          organization_form: {
            value: "weight",
          },
          area_targeted_by_org: {
            value: "weight"
          },
          organization_has_area: {
            value: "weight"
          },
          attacktype_used_by_org: {
            value: "weight"
          },
          organization_has_attacktype: {
            value: "weight"
          },
          domain_belongs_to_org: {
            value: "weight"
          },
          organization_has_domain: {
            value: "weight"
          },
          industry_targeted_by_org: {
            value: "weight"
          },
          organization_has_industry: {
            value: "weight"
          },
          ip_possessed_by_org: {
            value: "weight"
          },
          organization_has_ip: {
            value: "weight"
          },
          sha256_possessed_by_org: {
            value: "weight"
          },
          organization_has_sha256: {
            value: "weight"
          },
          area_has_organization: {
            value: "weight"
          }
        },
        initialCypher: 'MATCH (n)-[r:organization_from]->(m) RETURN n,r,m'
      };
      this.viz = new NeoVis(config);
      this.viz.render();
      console.log(this.viz);
    },
    generateCypher(value) {
      this.values = value;
      let length = this.values.length
      console.log(this.values)
      this.from = ''
      this.relation = ''
      this.to = ''
      this.limit = ''
      let from = ''
      let relation = ''
      let to = ''
      if (length > 0) {
        this.from = this.values[0]
        from = ':' + this.from
      }
      if (length > 1) {
        this.relation = this.values[1]
        relation = ':' + this.relation
      }
      if (length > 2) {
        this.to = this.values[2]
        to = ':' + this.to
      }
      if (this.limitNum === undefined) {
        this.limit = ''
      } else {
        this.limit = ' LIMIT ' + this.limitNum
      }
      console.log(this.limitNum)
      console.log(this.limit)
      this.cypher = 'MATCH (n' + from + ')-[r' + relation + ']->(m' + to + ')' + this.searchValueCypher + 'RETURN n,r,m' + this.limit
      console.log(this.cypher)
    },
    valueSearch(searchValue) {
      this.searchValue = searchValue
      if (this.searchValue != '') {
        this.searchLabel = ''
        this.searchValueCypher = ''
        this.searchValueCypher = ' WHERE n.value="' + this.searchValue + '" '
      } else {
        this.searchValueCypher = ''
      }
      console.log('======' + this.searchValueCypher)
    }
  },
}
</script>

<style lang="scss">

.myDiv {
  width: 100vw;
  height: 800px;
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

</style>