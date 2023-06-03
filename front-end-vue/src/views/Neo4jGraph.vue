<template>
  <div class="myDiv">
    <el-row class="search-row row_style flex column justify-center align-start">
      <div style="margin: 5px" class="flex row justify-start align-center">
        <span class="mr-5 ml-5">按类型查询</span>
        <el-cascader
            class="ml-5"
            :options="options"
            :props="{ checkStrictly: true }"
            clearable
            @change="generateTempCypher"></el-cascader>
        <el-input-number class="ml-5" v-model="limitNum" clearable placeholder="限制数量" style="width: 100px"
                         @blur="changeLimitNum"></el-input-number>
        <el-button class="ml-5" type="primary" @click="search">搜索</el-button>
        <el-button class="ml-5" type="text" @click="stabilize">Stabilize</el-button>
      </div>
      <div style="margin: 5px" class="flex row justify-start align-center">
        <span class="ml-5" style="margin-right: 20px">实体名称</span>
        <el-input class="ml-5" v-model="searchValue" type="String" clearable placeholder="搜索内容" style="width: 150px"
                  @change="generateSearchValueCypher"></el-input>
        <el-button class="ml-5" type="primary" @click="search">搜索</el-button>
      </div>
    </el-row>
    <Neo4j
        style="margin-top: 10px;"
        ref="neo4j"
        :cypher=this.cypher
    ></Neo4j>
    <div class="cypher-text">{{ cypher }}</div>
  </div>
</template>

<script>
// 引入jquery.js
import $ from 'jquery';

// 引入neovis.js
import NeoVis from 'neovis.js'
import Neo4j from "@/components/Neo4j.vue";

export default {
  name: "Neo4jGraph",
  components: {
    Neo4j
  },
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
      tempCypher: '',
      from: '',
      relation: '',
      to: '',
      limitNum: 50,
      searchValue: '',
      searchValueCypher: '',
      limitCypher: ''
    }
  },
  methods: {
    search() {
      this.generateSearchValueCypher(this.searchValue)
      this.changeLimitNum()
      this.generateTempCypher(this.values)
      this.generateCypher()
    },
    changeLimitNum() {
      this.limitCypher = ''
      if (this.limitNum === undefined) {
        this.limitCypher = ''
      } else {
        this.limitCypher = ' LIMIT ' + this.limitNum
      }
      console.log(this.limitCypher)
    },
    generateTempCypher(value) {
      this.values = value;
      let length = this.values.length
      console.log(this.values)
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
      this.tempCypher = 'MATCH (n' + from + ')-[r' + relation + ']->(m' + to + ')'
      console.log(this.tempCypher)
    },
    generateSearchValueCypher(searchValue) {
      this.searchValue = searchValue
      if (this.searchValue != '') {
        this.searchValueCypher = ''
        this.searchValueCypher = ' WHERE n.value="' + this.searchValue + '" '
      } else {
        this.searchValueCypher = ''
      }
      console.log('======' + this.searchValueCypher)
    },
    generateCypher() {
      this.cypher = this.tempCypher + this.searchValueCypher + 'RETURN n,r,m' + this.limitCypher
    },
    stabilize() {
      this.$refs.neo4j.stabilize()
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
.cypher-text {
  display: inline-block;
  margin: 10px auto;
  font-size: 20px;
  font-family: "Arial", "Microsoft YaHei", "黑体", "宋体", sans-serif;
}
</style>