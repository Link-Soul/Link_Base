<template>
  <div class="calculator-app">
    <div class="calculator-display">
      <div class="display-value">{{ displayValue }}</div>
      <div class="display-expression">{{ expression }}</div>
    </div>
    
    <div class="calculator-buttons">
      <div class="button-row">
        <button class="btn btn-function" @click="clear">AC</button>
        <button class="btn btn-function" @click="toggleSign">+/-</button>
        <button class="btn btn-function" @click="percentage">%</button>
        <button class="btn btn-operator" @click="setOperator('/')">÷</button>
      </div>
      
      <div class="button-row">
        <button class="btn btn-number" @click="appendNumber('7')">7</button>
        <button class="btn btn-number" @click="appendNumber('8')">8</button>
        <button class="btn btn-number" @click="appendNumber('9')">9</button>
        <button class="btn btn-operator" @click="setOperator('*')">×</button>
      </div>
      
      <div class="button-row">
        <button class="btn btn-number" @click="appendNumber('4')">4</button>
        <button class="btn btn-number" @click="appendNumber('5')">5</button>
        <button class="btn btn-number" @click="appendNumber('6')">6</button>
        <button class="btn btn-operator" @click="setOperator('-')">−</button>
      </div>
      
      <div class="button-row">
        <button class="btn btn-number" @click="appendNumber('1')">1</button>
        <button class="btn btn-number" @click="appendNumber('2')">2</button>
        <button class="btn btn-number" @click="appendNumber('3')">3</button>
        <button class="btn btn-operator" @click="setOperator('+')">+</button>
      </div>
      
      <div class="button-row">
        <button class="btn btn-number btn-zero" @click="appendNumber('0')">0</button>
        <button class="btn btn-number" @click="appendDecimal">.</button>
        <button class="btn btn-operator" @click="calculate">=</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'

const currentValue = ref('0')
const previousValue = ref('')
const operator = ref('')
const waitingForOperand = ref(false)

const displayValue = computed(() => {
  const number = parseFloat(currentValue.value)
  if (isNaN(number)) return currentValue.value
  return number.toLocaleString()
})

const expression = computed(() => {
  if (!operator.value && !previousValue.value) return ''
  return `${previousValue.value} ${operator.value} ${waitingForOperand.value ? '' : currentValue.value}`
})

const clear = () => {
  currentValue.value = '0'
  previousValue.value = ''
  operator.value = ''
  waitingForOperand.value = false
}

const toggleSign = () => {
  const number = parseFloat(currentValue.value)
  if (number === 0) return
  currentValue.value = String(number * -1)
}

const percentage = () => {
  const number = parseFloat(currentValue.value)
  if (number === 0) return
  currentValue.value = String(number / 100)
}

const appendNumber = (num) => {
  if (waitingForOperand.value) {
    currentValue.value = num
    waitingForOperand.value = false
  } else {
    currentValue.value = currentValue.value === '0' ? num : currentValue.value + num
  }
}

const appendDecimal = () => {
  if (waitingForOperand.value) {
    currentValue.value = '0.'
    waitingForOperand.value = false
  } else if (currentValue.value.indexOf('.') === -1) {
    currentValue.value += '.'
  }
}

const setOperator = (nextOperator) => {
  const inputValue = parseFloat(currentValue.value)

  if (previousValue.value === '') {
    previousValue.value = currentValue.value
  } else if (operator.value) {
    const currentValueFloat = parseFloat(currentValue.value)
    const previousValueFloat = parseFloat(previousValue.value)
    let newValue = previousValueFloat

    if (operator.value === '+') {
      newValue = previousValueFloat + currentValueFloat
    } else if (operator.value === '-') {
      newValue = previousValueFloat - currentValueFloat
    } else if (operator.value === '*') {
      newValue = previousValueFloat * currentValueFloat
    } else if (operator.value === '/') {
      newValue = previousValueFloat / currentValueFloat
    }

    currentValue.value = String(newValue)
    previousValue.value = currentValue.value
  }

  waitingForOperand.value = true
  operator.value = nextOperator
}

const calculate = () => {
  const inputValue = parseFloat(currentValue.value)

  if (previousValue.value !== '' && operator.value) {
    const previousValueFloat = parseFloat(previousValue.value)
    let newValue = previousValueFloat

    if (operator.value === '+') {
      newValue = previousValueFloat + inputValue
    } else if (operator.value === '-') {
      newValue = previousValueFloat - inputValue
    } else if (operator.value === '*') {
      newValue = previousValueFloat * inputValue
    } else if (operator.value === '/') {
      newValue = previousValueFloat / inputValue
    }

    currentValue.value = String(newValue)
    previousValue.value = ''
    operator.value = ''
    waitingForOperand.value = true
  }
}
</script>

<style scoped>
.calculator-app {
  display: flex;
  flex-direction: column;
  height: 100%;
  background: var(--color-surface);
  border-radius: var(--border-radius);
  overflow: hidden;
}

.calculator-display {
  background: var(--color-background);
  padding: 20px;
  text-align: right;
  border-bottom: 1px solid var(--color-border);
}

.display-value {
  font-size: 2.5rem;
  font-weight: 300;
  color: var(--color-text);
  margin-bottom: 8px;
  min-height: 60px;
  display: flex;
  align-items: center;
  justify-content: flex-end;
}

.display-expression {
  font-size: 1rem;
  color: var(--color-text-secondary);
  min-height: 24px;
}

.calculator-buttons {
  flex: 1;
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.button-row {
  display: flex;
  gap: 12px;
}

.btn {
  flex: 1;
  height: 60px;
  border: none;
  border-radius: 50%;
  font-size: 1.25rem;
  font-weight: 500;
  cursor: pointer;
  transition: all var(--duration-fast) var(--easing);
  display: flex;
  align-items: center;
  justify-content: center;
}

.btn:hover {
  transform: scale(1.05);
}

.btn:active {
  transform: scale(0.95);
}

.btn-number {
  background: var(--color-background);
  color: var(--color-text);
  border: 1px solid var(--color-border);
}

.btn-number:hover {
  background: var(--color-surface);
}

.btn-function {
  background: var(--color-surface);
  color: var(--color-text);
  border: 1px solid var(--color-border);
}

.btn-function:hover {
  background: var(--color-border);
}

.btn-operator {
  background: var(--color-primary);
  color: white;
}

.btn-operator:hover {
  background: var(--color-secondary);
}

.btn-zero {
  border-radius: 50px;
  flex: 2;
}
</style>