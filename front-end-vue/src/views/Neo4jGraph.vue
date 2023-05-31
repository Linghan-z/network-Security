<template>
  <div class="myDiv">
    <div ref="viz" id="viz"></div>
    Cypher query: <textarea rows="4" cols=50 id="cypher"></textarea><br>
    <input type="submit" value="Submit" id="reload" @click="submit">
    <input type="submit" value="Stabilize" id="stabilize" @click="stabilize">
  </div>
</template>

<script>
// 引入jquery.js
// import $ from 'jquery';

// 引入neovis.js
import Neovis from 'neovis.js';
// import 'vis-network/dist/dist/vis-network.min.css';

export default {
  name: "Neo4jGraph",
  components: {},
  props: {},
  data() {
    return {
      viz: {} //定义一个viz对象
    }
  },
  mounted() {
      this.draw();
  }, //渲染
  methods: {
    submit() {
      var cypher = $("#cypher").val();
      if (cypher.length > 3) {
        this.viz.renderWithCypher(cypher);
      } else {
        console.log("reload");
        this.viz.reload();

      }
    },
    stabilize() {
      this.viz.stabilize();
    },
    draw() {
      var config = {
        container_id: "viz",
        server_url: "bolt://localhost:7687",
        server_user: "neo4j",
        server_password: "123456",
        labels: {
          //"Character": "name",
          "Area": {
            "caption": "name",
            "size": "pagerank",
            "community": "community"
            //"sizeCypher": "MATCH (n) WHERE id(n) = {id} MATCH (n)-[r]-() RETURN sum(r.weight) AS c"
          },
          "Organization": {
            "caption": "name",
            "size": "pagerank",
            "community": "community"
          }
        },
        relationships: {
          "organization_from": {
            "thickness": "weight",
            "caption": false
          }
        },
        //查询节点的语句，写成你们的
        initial_cypher: "match (n)-[r]->(m) return n,r,m;"

      };

      this.viz = new NeoVis(config);
      this.viz.render();
      console.log(this.viz);
    }
  },
}
</script>

<style lang="scss">

.myDiv {
  width: 800px;
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