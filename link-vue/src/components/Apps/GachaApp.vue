<template>
  <div class="gacha-app">
    <div class="gacha-header">
      <h2>明日方舟抽卡数据统计</h2>
      <div class="gacha-controls">
        <button @click="submitJSON" class="btn btn-primary">从官网刷新数据</button>
        <div class="input-group">
          <input
            v-model="jsonFileUrl"
            placeholder="请输入小黑盒数据地址"
            class="form-control"
          />
          <button @click="getData" class="btn btn-secondary">从小黑盒获取数据</button>
        </div>
        <button @click="resetTotalMessageTable" class="btn btn-warning">
          重置数据
        </button>
      </div>
    </div>

    <!-- 加载遮罩 -->
    <div v-if="loading" class="loading-mask">
      <div class="loading-spinner">数据查询中，请稍后</div>
    </div>

    <div class="gacha-content">
      <div class="gacha-left">
        <!-- 柱状图 -->
        <div class="chart-container">
          <div ref="getNumByPoolChart" class="chart"></div>
          <div class="chart-controls">
            <button
              v-if="showTotalView"
              @click="change(true)"
              class="btn btn-sm btn-info"
            >
              点击切换为各卡池细则
            </button>
            <button
              v-if="!showTotalView"
              @click="change(false)"
              class="btn btn-sm btn-info"
            >
              点击切换为总抽数
            </button>
          </div>
        </div>

        <!-- 数据统计表格 -->
        <div class="stats-table">
          <table class="table table-striped">
            <tbody>
              <tr>
                <td colspan="2">
                  至今为止已过去 <span class="highlight">{{ totalTime }}</span>
                </td>
              </tr>
              <tr>
                <td colspan="2">
                  总计
                  <span class="highlight text-primary">{{
                    messageTotal.sum
                  }}</span>
                  抽， 共消耗合成玉：<span class="highlight text-warning">{{
                    messageTotal.stones
                  }}</span>
                </td>
              </tr>
              <tr>
                <td>
                  六星共计
                  <span class="highlight text-danger">{{
                    messageTotal.six
                  }}</span>
                  人， 概率
                  <span class="highlight">{{ messageTotal.sixRate }}%</span>，
                  平均出货
                  <span class="highlight">{{ messageTotal.sixAvg }}</span> 抽
                </td>
                <td>
                  四星共计
                  <span class="highlight text-info">{{
                    messageTotal.four
                  }}</span>
                  人， 概率
                  <span class="highlight">{{ messageTotal.fourRate }}%</span>
                </td>
              </tr>
              <tr>
                <td>
                  五星共计
                  <span class="highlight text-warning">{{
                    messageTotal.five
                  }}</span>
                  人， 概率
                  <span class="highlight">{{ messageTotal.fiveRate }}%</span>，
                  平均出货
                  <span class="highlight">{{ messageTotal.fiveAvg }}</span> 抽
                </td>
                <td>
                  三星共计
                  <span class="highlight text-secondary">{{
                    messageTotal.three
                  }}</span>
                  人， 概率
                  <span class="highlight">{{ messageTotal.threeRate }}%</span>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <!-- 饼图 -->
        <div class="chart-container">
          <div ref="RespectiveNumData" class="chart"></div>
        </div>
      </div>

      <div class="gacha-right">
        <div class="card-pool-list">
          <div
            v-for="(item, index) in cardMsgByPoolList"
            :key="index"
            class="pool-item"
          >
            <div class="pool-header">
              <span class="pool-name">{{ item.poolName }}</span>
              <span class="pool-total">{{ item.total }}</span>
            </div>
            <div v-if="item.six && item.six.length > 0" class="pool-six">
              <div
                v-for="(card, cardIndex) in item.six"
                :key="cardIndex"
                class="six-card-item"
              >
                <span class="card-name">{{ card.name }}</span>
                <span class="card-num">{{ card.num }}</span>
              </div>
            </div>
            <div v-else class="no-six">该卡池暂无六星</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from "vue";
import * as echarts from "echarts";

// 响应式数据
const loading = ref(false);
const jsonFileUrl = ref("");
const totalTime = ref("");
const showTotalView = ref(true);
const getNumByPoolChart = ref(null);
const RespectiveNumData = ref(null);

// 数据
const data = ref([]);
const getNumByPoolData = ref([]);
const RespectiveNumDataArray = ref([]);
const cardMsgByPoolList = ref([]);

// 统计数据
const messageTotal = ref({
  sum: 0,
  stones: 0,
  six: 0,
  sixRate: "0.00",
  sixAvg: "0.0",
  five: 0,
  fiveRate: "0.00",
  fiveAvg: "0.0",
  four: 0,
  fourRate: "0.00",
  three: 0,
  threeRate: "0.00",
});

// 图表实例
let getNumByPoolChartInstance = null;
let RespectiveNumDataChartInstance = null;

// 图表配置
const selectedT = ref({
  total: true,
  六星: false,
  五星: false,
  四星: false,
  三星: false,
});
const selectedF = ref({
  total: false,
  六星: true,
  五星: true,
  四星: true,
  三星: true,
});

const getNumByPoolOption = ref({
  grid: {
    left: "10%",
    right: "10%",
  },
  title: {
    text: "各卡池寻访次数",
  },
  tooltip: {
    trigger: "item",
  },
  legend: {
    data: ["total", "六星", "五星", "四星", "三星"],
    selected: {
      total: true,
      六星: false,
      五星: false,
      四星: false,
      三星: false,
    },
  },
  xAxis: {
    data: [],
    axisLabel: {
      interval: 0,
      rotate: -30,
    },
  },
  yAxis: {},
  series: [
    {
      name: "total",
      type: "bar",
      data: [],
      label: {
        show: true,
        position: "top",
        formatter: "{c}",
      },
      itemStyle: {
        color: "#4A90E2",
      },
    },
    {
      name: "六星",
      type: "bar",
      data: [],
      label: {
        show: true,
        position: "top",
        formatter: "{c}",
      },
      itemStyle: {
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          { offset: 1, color: "#ee7800" },
          { offset: 0, color: "#e80505" },
        ]),
      },
    },
    {
      name: "五星",
      type: "bar",
      data: [],
      label: {
        show: true,
        position: "top",
        formatter: "{c}",
      },
      itemStyle: {
        color: "#f39800",
      },
    },
    {
      name: "四星",
      type: "bar",
      data: [],
      label: {
        show: true,
        position: "top",
        formatter: "{c}",
      },
      itemStyle: {
        color: "#a1a1bd",
      },
    },
    {
      name: "三星",
      type: "bar",
      data: [],
      label: {
        show: true,
        position: "top",
        formatter: "{c}",
      },
      itemStyle: {
        color: "#74aff8",
      },
    },
  ],
});

const optionForRespectiveNumDataChart = ref({
  series: [
    {
      type: "pie",
      data: [],
    },
  ],
});

// 方法
const showMask = () => {
  loading.value = true;
};

const hideMask = () => {
  loading.value = false;
};

const submitJSON = async () => {
  showMask();
  try {
    // 调用后端API
    const response = await fetch("/api/insert", {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    });
    const result = await response.json();

    hideMask();
    onLoad();
  } catch (error) {
    hideMask();
    alert("更新数据失败，请检查后端服务是否正常运行");
  }
};

const getData = async () => {
  if (!jsonFileUrl.value.trim()) {
    alert("请输入数据地址！");
    return;
  }

  showMask();
  try {
    // 调用后端API
    const response = await fetch(
      `/api/insertFromXhhWeb?fileUrl=${encodeURIComponent(jsonFileUrl.value)}`,
      {
        method: "Post",
        headers: {
          "Content-Type": "application/json",
        },
      }
    );
    const result = await response.json();

    hideMask();
    onLoad();
  } catch (error) {
    hideMask();
    alert("获取数据失败，请检查数据地址和后端服务是否正常");
  }
};

const change = (TF) => {
  showTotalView.value = TF;
  if (TF) {
    getNumByPoolOption.value.legend.selected = selectedF.value;
  } else {
    getNumByPoolOption.value.legend.selected = selectedT.value;
  }
  if (getNumByPoolChartInstance) {
    getNumByPoolChartInstance.setOption(getNumByPoolOption.value);
  }
};

const resetTotalMessageTable = () => {
  changeTotalMessageTable("total");
};

const changeTotalMessageTable = (name = "total") => {
  let currentData = {};

  RespectiveNumDataArray.value.forEach((item) => {
    if (item.pool === name) {
      currentData = item;
    }
  });

  // 更新统计数据
  messageTotal.value.sum = currentData.sum || 0;
  messageTotal.value.stones = currentData.sum * 600;
  messageTotal.value.six = currentData.six || 0;
  messageTotal.value.five = currentData.five || 0;
  messageTotal.value.four = currentData.four || 0;
  messageTotal.value.three = currentData.three || 0;

  const sum = currentData.sum || 1;
  messageTotal.value.sixRate = (((currentData.six || 0) * 100) / sum).toFixed(
    2
  );
  messageTotal.value.fiveRate = (((currentData.five || 0) * 100) / sum).toFixed(
    2
  );
  messageTotal.value.fourRate = (((currentData.four || 0) * 100) / sum).toFixed(
    2
  );
  messageTotal.value.threeRate = (
    ((currentData.three || 0) * 100) /
    sum
  ).toFixed(2);

  messageTotal.value.sixAvg = (sum / (currentData.six || 1)).toFixed(1);
  messageTotal.value.fiveAvg = (sum / (currentData.five || 1)).toFixed(1);

  // 更新饼图
  if (RespectiveNumDataChartInstance) {
    const optionData = optionForRespectiveNumDataChart.value.series[0].data;
    optionData.splice(0);
    optionData.push({ name: "六星", value: currentData.six || 0 });
    optionData.push({ name: "五星", value: currentData.five || 0 });
    optionData.push({ name: "四星", value: currentData.four || 0 });
    optionData.push({ name: "三星", value: currentData.three || 0 });
    RespectiveNumDataChartInstance.setOption(
      optionForRespectiveNumDataChart.value
    );
  }
};

const setNumByPoolChart = (res) => {
  const ser = getNumByPoolOption.value.series;
  const xAxis = getNumByPoolOption.value.xAxis.data;
  xAxis.splice(0);
  ser[0].data.splice(0);
  ser[1].data.splice(0);
  ser[2].data.splice(0);
  ser[3].data.splice(0);
  ser[4].data.splice(0);

  for (let i = 0; i < res.length - 1; i++) {
    xAxis.push(res[i].pool);
    ser[0].data.push(res[i].sum);
    ser[1].data.push(res[i].six);
    ser[2].data.push(res[i].five);
    ser[3].data.push(res[i].four);
    ser[4].data.push(res[i].three);
  }

  if (getNumByPoolChartInstance) {
    getNumByPoolChartInstance.setOption(getNumByPoolOption.value);
  }
};

const cardMsgByPoolListTable = (res) => {
  cardMsgByPoolList.value = res.map((item) => {
    if (item.six) {
      return {
        ...item,
        six: [...item.six].reverse(),
      };
    }
    return item;
  });
};

const onLoad = async () => {
  try {
    // 调用后端API获取抽卡信息
    const cardResponse = await fetch("/api/getCardMessageById?uid=86670999", {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    });

    if (!cardResponse.ok) {
      throw new Error(`抽卡信息请求失败: ${cardResponse.status}`);
    }

    const cardData = await cardResponse.text();

    let res;
    try {
      res = JSON.parse(cardData);
    } catch (jsonError) {
      throw new Error("抽卡信息数据格式错误");
    }

    data.value = res.reverse();
    data.value.forEach((item) => {
      if (item.six !== undefined) {
        item.six.reverse();
      }
    });
    cardMsgByPoolListTable(res);

    // 获取柱状图数据
    const chartResponse = await fetch("/api/queryRespectiveNum", {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    });

    if (!chartResponse.ok) {
      throw new Error(`柱状图数据请求失败: ${chartResponse.status}`);
    }

    const chartData = await chartResponse.text();

    let chartRes;
    try {
      chartRes = JSON.parse(chartData);
    } catch (jsonError) {
      throw new Error("柱状图数据格式错误");
    }

    RespectiveNumDataArray.value = chartRes;
    changeTotalMessageTable();
    setNumByPoolChart(chartRes);

    // 获取时间跨度
    const timeResponse = await fetch("/api/getTotalTime", {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    });

    if (!timeResponse.ok) {
      throw new Error(`时间跨度请求失败: ${timeResponse.status}`);
    }

    const timeData = await timeResponse.text();

    // 时间跨度直接返回字符串，不需要JSON.parse
    totalTime.value = timeData;
  } catch (error) {
    // 如果API调用失败，使用模拟数据
    const mockData = [
      {
        poolName: "常规寻访",
        pool: "standard",
        total: 100,
        sum: 100,
        six: 5,
        five: 15,
        four: 40,
        three: 40,
        six: [
          { name: "银灰", num: 2 },
          { name: "能天使", num: 1 },
          { name: "艾雅法拉", num: 1 },
          { name: "伊芙利特", num: 1 },
        ],
      },
      {
        poolName: "限定寻访",
        pool: "limited",
        total: 80,
        sum: 80,
        six: 3,
        five: 12,
        four: 35,
        three: 30,
        six: [
          { name: "史尔特尔", num: 1 },
          { name: "浊心斯卡蒂", num: 1 },
          { name: "凯尔希", num: 1 },
        ],
      },
    ];

    const mockRespectiveNumData = [
      {
        pool: "total",
        sum: 180,
        six: 8,
        five: 27,
        four: 75,
        three: 70,
      },
      {
        pool: "standard",
        sum: 100,
        six: 5,
        five: 15,
        four: 40,
        three: 40,
      },
      {
        pool: "limited",
        sum: 80,
        six: 3,
        five: 12,
        four: 35,
        three: 30,
      },
    ];

    totalTime.value = "120天";

    data.value = mockData;
    cardMsgByPoolListTable(mockData);
    RespectiveNumDataArray.value = mockRespectiveNumData;

    changeTotalMessageTable();
    setNumByPoolChart(mockRespectiveNumData);
  }
};

// 初始化图表
const initCharts = () => {
  if (getNumByPoolChart.value) {
    getNumByPoolChartInstance = echarts.init(getNumByPoolChart.value);
    getNumByPoolChartInstance.setOption(getNumByPoolOption.value);

    // 点击事件
    getNumByPoolChartInstance.on("click", (params) => {
      changeTotalMessageTable(params.name);
    });
  }

  if (RespectiveNumData.value) {
    RespectiveNumDataChartInstance = echarts.init(RespectiveNumData.value);
    RespectiveNumDataChartInstance.setOption(
      optionForRespectiveNumDataChart.value
    );
  }
};

onMounted(async () => {
  await nextTick();
  initCharts();
  onLoad();

  // 监听窗口大小变化
  window.addEventListener("resize", () => {
    if (getNumByPoolChartInstance) {
      getNumByPoolChartInstance.resize();
    }
    if (RespectiveNumDataChartInstance) {
      RespectiveNumDataChartInstance.resize();
    }
  });
});
</script>

<style scoped>
.gacha-app {
  height: 100%;
  display: flex;
  flex-direction: column;
  background: var(--color-background);
  color: var(--color-text);
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif;
}

.gacha-header {
  padding: 20px;
  background: var(--color-surface);
  border-bottom: 1px solid var(--color-border);
}

.gacha-header h2 {
  margin: 0 0 15px 0;
  color: var(--color-text);
  font-size: 1.5rem;
}

.gacha-controls {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
  align-items: center;
}

.input-group {
  display: flex;
  gap: 5px;
}

.form-control {
  padding: 8px 12px;
  border: 1px solid var(--color-border);
  border-radius: 4px;
  background: var(--color-background);
  color: var(--color-text);
  min-width: 250px;
}

.form-control:focus {
  outline: none;
  border-color: var(--color-primary);
}

.btn {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.2s;
}

.btn-primary {
  background: var(--color-primary);
  color: white;
}

.btn-primary:hover {
  background: var(--color-primary-dark);
}

.btn-secondary {
  background: var(--color-secondary);
  color: white;
}

.btn-secondary:hover {
  background: var(--color-secondary-dark);
}

.btn-warning {
  background: #ffc107;
  color: #212529;
}

.btn-warning:hover {
  background: #e0a800;
}

.btn-info {
  background: #17a2b8;
  color: white;
}

.btn-info:hover {
  background: #138496;
}

.btn-sm {
  padding: 4px 8px;
  font-size: 12px;
}

.loading-mask {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.loading-spinner {
  background: var(--color-surface);
  padding: 20px 40px;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
}

.gacha-content {
  flex: 1;
  display: flex;
  padding: 20px;
  gap: 20px;
  overflow: auto;
}

.gacha-left {
  flex: 2;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.gacha-right {
  flex: 1;
  min-width: 300px;
}

.chart-container {
  background: var(--color-surface);
  border-radius: 8px;
  padding: 15px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.chart {
  height: 400px;
  width: 100%;
}

.chart-controls {
  margin-top: 10px;
  text-align: center;
}

.stats-table {
  background: var(--color-surface);
  border-radius: 8px;
  padding: 15px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.table {
  width: 100%;
  border-collapse: collapse;
}

.table-striped tbody tr:nth-of-type(odd) {
  background: rgba(0, 0, 0, 0.05);
}

.table td {
  padding: 8px 12px;
  border: 1px solid var(--color-border);
}

.highlight {
  font-weight: bold;
  font-size: 1.1em;
}

.text-primary {
  color: #007bff;
}

.text-secondary {
  color: #6c757d;
}

.text-success {
  color: #28a745;
}

.text-danger {
  color: #dc3545;
}

.text-warning {
  color: #ffc107;
}

.text-info {
  color: #17a2b8;
}

.card-pool-list {
  background: var(--color-surface);
  border-radius: 8px;
  padding: 15px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  max-height: 600px;
  overflow-y: auto;
}

.pool-item {
  margin-bottom: 15px;
  border: 1px solid var(--color-border);
  border-radius: 6px;
  overflow: hidden;
}

.pool-header {
  background: #bce672;
  padding: 10px 15px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: bold;
}

.pool-name {
  flex: 1;
}

.pool-total {
  font-size: 1.2em;
  color: #2c5f2d;
}

.pool-six {
  background: var(--color-background);
}

.six-card-item {
  display: flex;
  justify-content: space-between;
  padding: 8px 15px;
  border-bottom: 1px solid var(--color-border);
}

.six-card-item:last-child {
  border-bottom: none;
}

.card-name {
  flex: 1;
}

.card-num {
  font-weight: bold;
  color: var(--color-primary);
}

.no-six {
  padding: 15px;
  text-align: center;
  color: var(--color-text-secondary);
  font-style: italic;
}

/* 响应式设计 */
@media (max-width: 1024px) {
  .gacha-content {
    flex-direction: column;
  }

  .gacha-left,
  .gacha-right {
    flex: 1;
    width: 100%;
  }
}

@media (max-width: 768px) {
  .gacha-header {
    padding: 15px;
  }

  .gacha-controls {
    flex-direction: column;
    align-items: stretch;
  }

  .input-group {
    flex-direction: column;
  }

  .form-control {
    min-width: auto;
  }

  .gacha-content {
    padding: 10px;
  }

  .chart {
    height: 300px;
  }
}
</style>
