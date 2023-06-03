<template>
  <div id="viz"></div>
</template>

<script>
import NeoVis from "neovis.js";

export default {
  name: "Neo4jGraph",
  props: {
    cypher: String,
  },
  watch: {
    cypher(newValue, oldValue) {
      console.log(newValue)
      this.submit()
    },
    deep: true
  },
  data() {
    return {
      viz: {},  //定义一个viz对象
    }
  },
  mounted() {
    this.draw();
  }, //渲染
  methods: {
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
        // initialCypher: 'MATCH (n)-[r:organization_from]->(m) RETURN n,r,m'
      };
      this.viz = new NeoVis(config);
      this.viz.render();
      console.log(this.viz);
    },
    submit() {
      this.viz.clearNetwork();
      if (this.cypher.length > 3) {
        this.viz.renderWithCypher(this.cypher);
      } else {
        console.log("reload");
        this.viz.reload();
      }
    },
    stabilize() {
      this.viz.stabilize();
    },
  }

}
</script>

<style scoped>

</style>