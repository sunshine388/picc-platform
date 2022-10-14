<template>
  <div class="full">
    <el-breadcrumb separator="/">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>信息统计</el-breadcrumb-item>
      <el-breadcrumb-item
        ><span class="currentpage">统计报表</span></el-breadcrumb-item
      >
    </el-breadcrumb>
    <div style="text-align: center">
      <el-tabs v-model="activeName" @tab-click="handleClick">
        <el-tab-pane label="置管数量统计" name="first">
          <div
            id="catheterChart"
            style="width: 800px; height: 400px; margin: auto"
          ></div>
        </el-tab-pane>
        <el-tab-pane label="维护数量统计" name="second">
          <div
            id="maintainChart"
            style="width: 800px; height: 400px; margin: auto"
          ></div>
        </el-tab-pane>
        <el-tab-pane label="非计划拔管率统计" name="third">
          <div
            id="extubationChart"
            style="width: 650px; height: 400px; margin: auto"
          ></div>
        </el-tab-pane>
        <el-tab-pane label="并发症数量和比例统计" name="four">
          <div style="display: flex; justify-content: space-around">
            <div id="companionChart1" style="width: 650px; height: 400px"></div>
            <div id="companionChart2" style="width: 900px; height: 400px"></div>
          </div>
        </el-tab-pane>
      </el-tabs>
      <div style="margin-top: 20px">
        <el-button @click="prev" icon="el-icon-arrow-left">上一年</el-button>
        <el-button @click="next"
          >下一年<i class="el-icon-arrow-right el-icon--right"></i
        ></el-button>
      </div>
    </div>
  </div>
</template>

<script>
import {
  getStatisticCatheter,
  getStatisticMaintain,
  getStatisticExtubation,
  getStatisticCompanion
} from '@/api/statistic';

export default {
  data() {
    return {
      activeName: 'first',
      catheter: {},
      maintain: {},
      extubation: {},
      companion: {},

      year: new Date().getFullYear(),
      startDate: '',
      endDate: ''
    };
  },
  watch: {
    catheter(val) {
      this.setStatisticCatheter(val);
    },
    maintain(val) {
      this.setStatisticMaintain(val);
    },
    extubation(val) {
      this.setStatisticExtubation(val);
    },
    companion(val) {
      this.setStatisticCompanion(val);
    }
  },
  mounted() {
    this.init();
  },
  methods: {
    init() {
      if (this.activeName === 'first') {
        this.getStatisticCatheter();
      } else if (this.activeName === 'second') {
        this.getStatisticMaintain();
      } else if (this.activeName === 'third') {
        this.getStatisticExtubation();
      } else if (this.activeName === 'four') {
        this.getStatisticCompanion();
      }
    },
    getStatisticCatheter() {
      getStatisticCatheter({
        year: this.year
      }).then((res) => {
        if (res.status === 200) {
          this.catheter = res.data;
          let arr = ['month', ...this.catheter.nurseList];
          this.catheter.catheter.seriesData.unshift(arr);
        }
      });
    },
    setStatisticCatheter() {
      let myChart = this.$echarts.init(
        document.getElementById('catheterChart')
      );
      const option = {
        timeline: {
          axisType: 'category',
          autoPlay: false,
          playInterval: 1500,
          data: this.catheter.monthList,
          label: {
            fontSize: 18
          }
        },
        baseOption: {
          dataset: {
            source: this.catheter.catheter.seriesData
          },
          title: {
            text: `${this.year}年1月用户置管情况查看`,
            left: 'center',
            textStyle: {
              fontSize: 24
            }
          },
          tooltip: {
            trigger: 'axis'
          },
          legend: {},
          grid: {
            left: '10%',
            bottom: '15%',
            containLabel: true
          },
          xAxis: [
            {
              type: 'category',
              data: this.catheter.nurseList,
              axisPointer: {
                type: 'shadow'
              },
              axisTick: {
                alignWithLabel: true
              }
            }
          ],
          yAxis: [
            {
              name: '数量',
              type: 'value',
              nameTextStyle: {
                fontSize: 18
              },
              axisLabel: {
                fontSize: 18
              },
              interval: 10
            }
          ],
          series: [
            {
              type: 'bar',
              seriesLayoutBy: 'row',
              encode: {
                x: 'month',
                y: '1月'
              }
            }
          ]
        },
        options: []
      };
      for (let n = 0; n < this.catheter.monthList.length; n++) {
        let month = this.catheter.monthList[n];
        let total = this.catheter.catheter.allList[n];
        option.options.push({
          title: {
            show: true,
            text: `${this.year}年${month}用户置管情况查看`,
            subtext: `总共：${total}例`,
            left: 'center',
            textStyle: {
              fontSize: 24
            }
          },
          series: [
            {
              type: 'bar',
              seriesLayoutBy: 'row',
              encode: {
                x: 'month',
                y: this.catheter.monthList[n]
              }
            }
          ]
        });
      }
      myChart.setOption(option);
    },
    getStatisticMaintain() {
      getStatisticMaintain({
        year: this.year
      }).then((res) => {
        if (res.status === 200) {
          this.maintain = res.data;
          let arr = ['month', ...this.maintain.nurseList];
          this.maintain.maintain.seriesData.unshift(arr);
        }
      });
    },
    setStatisticMaintain() {
      let myChart = this.$echarts.init(
        document.getElementById('maintainChart')
      );
      const option = {
        timeline: {
          axisType: 'category',
          autoPlay: false,
          playInterval: 1500,
          data: this.maintain.monthList,
          label: {
            fontSize: 18
          }
        },
        baseOption: {
          dataset: {
            source: this.maintain.maintain.seriesData
          },
          title: {
            text: `${this.year}年1月用户维护情况查看`,
            left: 'center',
            textStyle: {
              fontSize: 24
            }
          },
          tooltip: {
            trigger: 'axis'
          },
          legend: {},
          grid: {
            left: '10%',
            bottom: '15%',
            containLabel: true
          },
          xAxis: [
            {
              type: 'category',
              data: this.maintain.nurseList,
              axisPointer: {
                type: 'shadow'
              },
              axisTick: {
                alignWithLabel: true
              }
            }
          ],
          yAxis: [
            {
              name: '数量',
              type: 'value',
              nameTextStyle: {
                fontSize: 18
              },
              axisLabel: {
                fontSize: 18
              },
              interval: 50
            }
          ],
          series: [
            {
              type: 'bar',
              seriesLayoutBy: 'row',
              encode: {
                x: 'month',
                y: '1月'
              }
            }
          ]
        },
        options: []
      };
      for (let n = 0; n < this.maintain.monthList.length; n++) {
        let month = this.maintain.monthList[n];
        let total = this.maintain.maintain.allList[n];
        option.options.push({
          title: {
            show: true,
            text: `${this.year}年${month}用户维护情况查看`,
            subtext: `总共：${total}例`,
            left: 'center',
            textStyle: {
              fontSize: 24
            }
          },
          series: [
            {
              type: 'bar',
              seriesLayoutBy: 'row',
              encode: {
                x: 'month',
                y: this.maintain.monthList[n]
              }
            }
          ]
        });
      }
      myChart.setOption(option);
    },
    getStatisticExtubation() {
      getStatisticExtubation({
        year: this.year
      }).then((res) => {
        if (res.status === 200) {
          this.extubation = res.data;
        }
      });
    },
    setStatisticExtubation() {
      let myChart = this.$echarts.init(
        document.getElementById('extubationChart')
      );
      const option = {
        timeline: {
          axisType: 'category',
          autoPlay: false,
          playInterval: 1500,
          data: this.extubation.monthList,
          label: {
            fontSize: 18
          }
        },
        baseOption: {
          title: {
            text: `${this.year}年1月用户拔管情况查看`,
            left: 'center',
            textStyle: {
              fontSize: 24
            }
          },
          tooltip: {
            trigger: 'item'
          },
          legend: {
            orient: 'vertical',
            left: 'left'
          },
          series: [
            {
              type: 'pie'
            }
          ]
        },
        options: []
      };
      for (let n = 0; n < this.extubation.monthList.length; n++) {
        let month = this.extubation.monthList[n];
        let total = this.extubation.allList[n];
        option.options.push({
          title: {
            show: true,
            text: `${this.year}年${month}用户拔管情况查看`,
            subtext: `总共：${total}例`,
            left: 'center',
            textStyle: {
              fontSize: 24
            }
          },
          series: [
            {
              type: 'pie',
              data: this.extubation.extubation[n]
            }
          ]
        });
      }
      myChart.setOption(option);
    },
    getStatisticCompanion() {
      getStatisticCompanion({
        year: this.year
      }).then((res) => {
        if (res.status === 200) {
          this.companion = res.data;
          let arr = ['month', ...this.companion.companionBar.xAxisData];
          this.companion.companionBar.seriesData.unshift(arr);
        }
      });
    },
    setStatisticCompanion() {
      let myChart1 = this.$echarts.init(
        document.getElementById('companionChart1')
      );
      let myChart2 = this.$echarts.init(
        document.getElementById('companionChart2')
      );
      const option1 = {
        timeline: {
          axisType: 'category',
          autoPlay: false,
          playInterval: 1500,
          data: this.companion.monthList,
          label: {
            fontSize: 18
          }
        },
        baseOption: {
          title: {
            text: `${this.year}年1月用户并发症情况查看`,
            left: 'center',
            textStyle: {
              fontSize: 24
            }
          },
          tooltip: {
            trigger: 'item'
          },
          legend: {
            orient: 'vertical',
            left: 'left',
            top: 30
          },
          series: [
            {
              type: 'pie'
            }
          ]
        },
        options: []
      };
      for (let n = 0; n < this.companion.monthList.length; n++) {
        let month = this.companion.monthList[n];
        let total = this.companion.allList[n];
        option1.options.push({
          title: {
            show: true,
            text: `${this.year}年${month}用户并发症情况查看`,
            subtext: `总共：${total}例`,
            left: 'center',
            textStyle: {
              fontSize: 24
            }
          },
          series: [
            {
              type: 'pie',
              data: this.companion.companion[n]
            }
          ]
        });
      }
      myChart1.setOption(option1);
      const option2 = {
        timeline: {
          axisType: 'category',
          autoPlay: false,
          playInterval: 1500,
          data: this.companion.monthList,
          label: {
            fontSize: 18
          }
        },
        baseOption: {
          dataset: {
            source: this.companion.companionBar.seriesData
          },
          title: {
            text: `${this.year}年1月用户并发症情况查看`,
            left: 'center',
            textStyle: {
              fontSize: 24
            }
          },
          tooltip: {
            trigger: 'axis'
          },
          legend: {},
          grid: {
            left: '10%',
            bottom: '15%',
            containLabel: true
          },
          xAxis: [
            {
              type: 'category',
              data: this.companion.companionBar.xAxisData,
              axisPointer: {
                type: 'shadow'
              },
              axisLabel: { interval: 0, rotate: 30 },
              axisTick: {
                alignWithLabel: true
              }
            }
          ],
          yAxis: [
            {
              name: '数量',
              type: 'value',
              nameTextStyle: {
                fontSize: 18
              },
              axisLabel: {
                fontSize: 18
              },
              interval: 5
            }
          ],
          series: [
            {
              type: 'bar',
              seriesLayoutBy: 'row',
              encode: {
                x: 'month',
                y: '1月'
              }
            }
          ]
        },
        options: []
      };
      for (let n = 0; n < this.companion.monthList.length; n++) {
        let month = this.companion.monthList[n];
        option2.options.push({
          title: {
            show: true,
            text: `${this.year}年${month}用户并发症情况查看`,
            left: 'center',
            textStyle: {
              fontSize: 24
            }
          },
          series: [
            {
              type: 'bar',
              seriesLayoutBy: 'row',
              encode: {
                x: 'month',
                y: this.companion.monthList[n]
              }
            }
          ]
        });
      }
      myChart2.setOption(option2);
    },
    handleClick() {
      this.init();
    },
    prev() {
      this.year--;
      this.init();
    },
    next() {
      this.year++;
      this.init();
    }
  }
};
</script>

<style scoped>
.search-input {
  width: 300px;
  margin-bottom: 15px;
}
.page {
  padding: 5px;
  background-color: #d3dce6;
  margin: 10px 0;
}
</style>
