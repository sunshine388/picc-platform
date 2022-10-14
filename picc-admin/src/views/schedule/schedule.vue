<template>
  <div class="full">
    <el-breadcrumb separator="/">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>排班管理</el-breadcrumb-item>
      <el-breadcrumb-item
        ><span class="currentpage">护士排班</span></el-breadcrumb-item
      >
    </el-breadcrumb>
    <div class="timeBox">
      <div style="text-align: right">
        <button v-print="'#printMe'">打印</button>
      </div>
      <div class="calendar-wrap">
        <div style="text-align: center; margin: 0 0 -26px -150px">
          （上午 8:00-11：30，下午 13:30-17:00）
        </div>

        <full-calendar
          :config="config"
          :events="events"
          ref="calendar"
          @event-selected="eventClick"
          id="printMe"
        ></full-calendar>
      </div>
      <el-dialog
        :close-on-click-modal="false"
        title="排班管理"
        :visible.sync="dialogFormVisible"
        center
      >
        <div style="display: flex; justify-content: space-around">
          <div>
            <el-table
              ref="multipleTable"
              :data="nurseList1"
              style="width: 100%"
              border
              @selection-change="handleSelectionChange"
            >
              <el-table-column type="selection" width="55"> </el-table-column>
              <el-table-column prop="username" label="姓名"> </el-table-column>
            </el-table>
            <div style="margin-top: 20px">
              <el-button @click="addToRight()">添加到右边</el-button>
            </div>
          </div>
          <div style="margin-left: 20px">
            <el-table :data="nurseList2" style="width: 100%" border>
              <el-table-column prop="nursename" label="姓名"> </el-table-column>
              <el-table-column label="人数限制" width="210">
                <template slot-scope="scope">
                  <el-input-number
                    v-model="scope.row.limit"
                    :min="1"
                    :max="20"
                    label="计数器"
                  ></el-input-number
                ></template>
              </el-table-column>
              <el-table-column label="操作">
                <template slot-scope="scope">
                  <el-button
                    size="mini"
                    type="danger"
                    icon="el-icon-delete"
                    @click="deleteOne(scope.row)"
                  ></el-button>
                </template>
              </el-table-column>
            </el-table>
            <div style="margin-top: 20px; text-align: right">
              <el-button @click="submit()">提交</el-button>
            </div>
          </div>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import { FullCalendar } from 'vue-full-calendar';
import {
  getNurseList,
  // getScheduleByJobnumber,
  postSchedule,
  getSchedule,
  getScheduleByTime,
  deleteScheduleOne
} from '@/api/nurses';
import { GetDateStr } from '@/utils/index';

export default {
  components: { FullCalendar },
  data() {
    return {
      nurseList1: [],
      nurseList2: [],
      initList: [],
      multipleSelection: [],
      nowClickDate: '',
      type: 0,
      dialogFormVisible: false,
      config: {
        locale: 'zh-cn',
        defaultView: 'month',
        header: {
          left: 'prevYear,prev,next,nextYear today',
          center: 'title',
          right: ''
        },
        editable: false, // 禁止拖动
        aspectRatio: 2.4,
        displayEventTime: false,
        customButtons: {
          today: {
            text: '回到今天',
            click: (e) => {
              this.today(e);
            }
          },
          prev: {
            text: 'prev',
            click: (e) => {
              this.prev(e);
            }
          },
          next: {
            text: 'next',
            click: (e) => {
              this.next(e);
            }
          },
          prevYear: {
            text: 'prevYear',
            click: (e) => {
              this.prevYear(e);
            }
          },
          nextYear: {
            text: 'nextYear',
            click: (e) => {
              this.nextYear(e);
            }
          }
        }
      },
      events: [
        //{
        //  id: 1,
        //  title: `上午`,
        //  start: "08:00:00", // 事件开始时间
        //  end: "11:30:00", // 事件结束时间
        //  color: "rgba(9, 9, 9, 0.2)",
        //},
        //{
        //  id: 2,
        //  title: "下午",
        //  start: "13:30:00", // 事件开始时间
        //  end: "17:00:00", // 事件结束时间editable: true
        //},
      ]
    };
  },
  methods: {
    init() {
      this.events = [];
      const start = this.$refs.calendar.fireMethod('getView').intervalStart._d;
      const end = this.$refs.calendar.fireMethod('getView').intervalEnd._d;
      const startYear = new Date(start).getFullYear();
      const startMonth = new Date(start).getMonth() + 1;
      const startDate = new Date(start).getDate();
      const startDay = startYear + '-' + startMonth + '-' + startDate;
      const endYear = new Date(end).getFullYear();
      const endMonth = new Date(end).getMonth() + 1;
      const endDate = new Date(end).getDate();
      const endDay = endYear + '-' + endMonth + '-' + endDate;
      const days =
        (new Date(endDay) - new Date(startDay)) / (1000 * 60 * 60 * 24); //天数
      getScheduleByTime({
        start: new Date(startDay).getTime(),
        end: new Date(endDay).getTime()
      }).then((res) => {
        if (res.status === 200) {
          for (let i = 0; i < days; i++) {
            let day = GetDateStr(startDay, i);
            let object1 = {
              id: new Date(day).getTime() + 1,
              type: 1,
              date: new Date(day).getTime(),
              title: '上午',
              start: `${this.formater(day)} 08:00:00`, // 事件开始时间
              end: `${this.formater(day)} 11:30:00`, // 事件结束时间
              color: 'rgba(9, 9, 9, 0.2)'
            };
            let object2 = {
              id: new Date(day).getTime() + 2,
              date: new Date(day).getTime(),
              type: 2,
              title: '下午',
              start: `${this.formater(day)} 13:30:00`, // 事件开始时间
              end: `${this.formater(day)} 17:00:00` // 事件结束时间editable: true
            };
            for (let j = 0; j < res.data.length; j++) {
              if (new Date(day).getTime() === res.data[j].date) {
                if (res.data[j].forenoon === 'true') {
                  object1.title = `${object1.title} ${res.data[j].nursename}`;
                }
                if (res.data[j].afternoon === 'true') {
                  object2.title = `${object2.title} ${res.data[j].nursename}`;
                }
              }
            }
            this.events.push(object1);
            this.events.push(object2);
          }
        } else {
          this.$message({
            type: 'warning',
            message: '删除失败'
          });
        }
      });
    },

    /// 点击事件
    eventClick(event) {
      this.nurseList1 = [];
      this.nurseList2 = [];
      this.dialogFormVisible = true;
      console.log(this.event);
      this.getNurseList(event.type, event.date);
    },
    getNurseList(type, date) {
      getNurseList({
        query: '',
        pagenum: 1,
        type: 1
      }).then((res) => {
        this.nurseList1 = res.data.nurses;
        this.getScheduleList(type, date);
      });
    },
    getScheduleList(type, date) {
      this.type = type;
      this.nowClickDate = date;
      getSchedule({ type: type, date: this.nowClickDate }).then((res) => {
        if (res.status === 200) {
          let keyArray = res.data;
          for (let i = 0; i < keyArray.length; i++) {
            this.nurseList1 = this.remove(
              this.nurseList1,
              keyArray[i].jobnumber
            );
            this.initList.push(keyArray[i]);
          }
          this.nurseList2 = keyArray;
        }
      });
    },
    //多选
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    addToRight() {
      if (this.multipleSelection.length > 0) {
        console.log(this.multipleSelection);
        let value = this.multipleSelection;
        for (let i = 0; i < value.length; i++) {
          this.nurseList1 = this.remove(this.nurseList1, value[i].jobnumber);
          this.nurseList2.push({
            jobnumber: value[i].jobnumber,
            nursename: value[i].username,
            date: this.nowClickDate,
            state: 'true',
            limit: 6
          });
        }
      }
    },
    deleteOne(row) {
      console.log(row.jobnumber);
      console.log(this.initList);
      if (this.search(this.initList, row.jobnumber)) {
        deleteScheduleOne({
          type: this.type,
          date: this.nowClickDate,
          jobnumber: row.jobnumber
        }).then((res) => {
          if (res.status === 200) {
            this.nurseList1.push({
              jobnumber: row.jobnumber,
              username: row.nursename
            });
            this.nurseList2 = this.remove(this.nurseList2, row.jobnumber);
            this.init();
          } else {
            this.$message({
              type: 'warning',
              message: '删除失败'
            });
          }
        });
      } else {
        this.nurseList1.push({
          jobnumber: row.jobnumber,
          username: row.nursename
        });
        this.nurseList2 = this.remove(this.nurseList2, row.jobnumber);
      }
    },
    submit() {
      let params = {
        type: this.type,
        date: this.nowClickDate,
        addList: JSON.stringify(this.nurseList2)
      };
      postSchedule(params).then((res) => {
        if (res.status === 200) {
          this.$message({
            type: 'success',
            message: '提交成功'
          });
          this.dialogFormVisible = false;
          this.init();
        }
      });
    },
    remove(array, val) {
      let newarray = [];
      for (let i = 0; i < array.length; i++) {
        if (array[i].jobnumber !== val) {
          newarray.push(array[i]);
        }
      }
      return newarray;
    },
    search(array, Key) {
      for (let i = 0; i < array.length; i++) {
        if (array[i].jobnumber === Key) {
          return true;
        }
      }
      return false;
    },
    formater(day) {
      const year = new Date(day).getFullYear();
      const m = new Date(day).getMonth() + 1;
      const d = new Date(day).getDate();
      const month = m < 10 ? '0' + m : m;
      const date = d < 10 ? '0' + d : d;
      return year + '-' + month + '-' + date;
    },
    today() {
      this.$refs.calendar.fireMethod('today');
      this.init();
    },
    prev() {
      this.$refs.calendar.fireMethod('prev');
      this.init();
    },
    next() {
      this.$refs.calendar.fireMethod('next');
      this.init();
    },
    prevYear() {
      this.$refs.calendar.fireMethod('prevYear');
      this.init();
    },
    nextYear() {
      this.$refs.calendar.fireMethod('nextYear');
      this.init();
    },
    //当天日期的点击事件
    //dayClick(date, allDay, jsEvent, view) {
    //  this.dialogFormVisible = true;
    //},
    //打印当前页面
    goPrint() {
      // eslint-disable-next-line no-undef
      printJS({
        printable: 'print',
        type: 'pdf',
        targetStyles: ['*'],
        ignoreElements: []
      });
    }
  },
  mounted() {
    this.init();
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
.fc-unthemed >>> .fc-content {
  white-space: normal;
}
.timebox {
  height: calc(100% - 60px);
}
</style>
