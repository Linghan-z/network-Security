<template>
  <div>
    <el-row class="search-row row_style flex row justify-start align-center">
      <span class="attribute_row"><i class="el-icon-search mr-5"></i>搜索 </span>
      <span class="search-text">要查询的类别：</span>
      <el-select v-model="label" placeholder="请选择" style="width: 150px;">
        <el-option
            v-for="item in entities"
            :key="item.value"
            :label="item.label"
            :value="item.value">
        </el-option>
      </el-select>
      <span style="width: 150px"></span>
      <span class="search-text">请输入要查询内容：</span>
      <el-input v-model="entityName" placeholder="请输入内容" @keyup.enter.native="searchEntity"
                style="width: 200px; margin-right: 50px"></el-input>
      <el-button class="ml-5" type="primary" @click="searchEntity">搜索</el-button>
    </el-row>
    <el-row class="header_row row_style flex row justify-start align-center">
      <span class="attribute_row"><i class="el-icon-collection-tag mr-5"></i>实体 </span>
      <span class="ml-20" style="margin: auto">{{ entity }}</span>
      <span class="attribute_row"></span>
    </el-row>
    <el-row class="detail-row row_style flex column justify-center align-start">
      <span class="attribute_row"><i class="el-icon-collection mr-5"></i>属性 </span>
      <el-table
          :data="tableData"
          :show-header="false"
          :cell-style="{height:'100px'}"
          style="width: 100%; font-size: 20px">
        <el-table-column
            fixed
            prop="attribute"
            label="title"
            width="150px">
        </el-table-column>
        <el-table-column
            prop="value">
        </el-table-column>
      </el-table>
    </el-row>
    <el-row class="neo4j-row row_style column">
      <span class="attribute_row"><i class="el-icon-share mr-5"></i>知识图谱 </span>
      <Neo4j
          :cypher=this.cypher
          :style="{height: neo4jHeight + 'px'}"></Neo4j>
    </el-row>
  </div>
</template>

<script>
import Neo4j from "@/components/Neo4j.vue";

export default {
  name: "Neo4jDetail",
  components: {
    Neo4j
  },
  data() {
    return {
      tableData: [],
      entities:
          [
            {
              value: 'organization',
              label: '组织'
            }, {
            value: 'area',
            label: '地区'
          }, {
            value: 'attacktype',
            label: '攻击方式'
          }, {
            value: 'industry',
            label: '行业'
          }, {
            value: 'ip',
            label: 'IP'
          }, {
            value: 'domain',
            label: 'Domain'
          }, {
            value: 'sha256',
            label: 'Sha256'
          }],
      label: 'organization',
      entityName: '',
      cypher: '',
      neo4jHeight: 0,
      entity: ''
    }
  },
  methods: {
    searchEntity() {
      try {
        console.log(this.label)
        console.log(this.entityName)
        this.request.get("/entity/" + this.label + "/" + this.entityName, {
          params: {
            entityName: this.entityName
          }
        }).then(res => {
          this.tableData = res.data
          delete res.data.id
          this.neo4jHeight = 700
          this.tableData = Object.entries(res.data).map(([key, value]) => ({attribute: key, value}));
          this.generateCypher()
          this.entity = this.entityName
          // console.log(this.neo4jHeight)
          // console.log(this.cypher)
        })
      } catch (error) {
        console.error(error)
      }
    },
    generateCypher() {
      if (this.label === 'organization') {
        this.cypher = "MATCH p=(a)-[r]->(b) WHERE a.value = '" + this.entityName + "' and r:organization_from RETURN p UNION " +
            "MATCH p=(a)-[r]->(b) WHERE a.value = '" + this.entityName + "' and r:organization_has_area RETURN p UNION " +
            "MATCH p=(a)-[r]->(b) WHERE a.value = '" + this.entityName + "' and r:organization_has_attacktype RETURN p UNION " +
            "MATCH p=(a)-[r]->(b) WHERE a.value = '" + this.entityName + "' and r:organization_has_industry RETURN p"
      } else {
        this.cypher = 'Match (a) WHERE a.value="' + this.entityName + '" RETURN a'
      }
    }
  }
}
</script>

<style scoped lang="scss">
.ml-20 {
  margin-left: 20px;
}

.row_style {
  margin: 10px auto;
  width: 80vw;
  border-radius: 25px;
}

.header_row {
  height: 100px;
  padding: 20px;
  background-color: cornflowerblue;
  border: 2px solid cornflowerblue;
  font-size: 35px;
  font-weight: bolder;
  font-family: "Arial", "Microsoft YaHei", "黑体", "宋体", sans-serif;
}

.attribute_row {
  min-width: 100px;
  font-size: 25px;
  font-weight: bold;
  font-family: "Arial", "Microsoft YaHei", "黑体", "宋体", sans-serif;
}

.detail-row {
  min-height: 100px;
  border: 2px solid #409EFF;
  padding: 20px;
}

.neo4j-row {
  min-height: 100px;
  border: 2px solid #8AC007;
  padding: 20px;
}

.search-row {
  min-height: 70px;
  border: 2px solid #d08120;
  padding: 20px;
}

.search-text {
  margin-left: 30px;
  margin-right: 10px;
  font-size: 17px;
  font-family: "Arial", "Microsoft YaHei", "黑体", "宋体", sans-serif;
}
</style>