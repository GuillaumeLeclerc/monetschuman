<template>
    <hello></hello>
</template>

<script>
import Hello from './components/Hello';

export default {
  components: {
    Hello,
  },
};
</script>

<style>
html, body{
  height: 100%;
  width: 100%;
  margin: 0;
  padding: 0;
}

#app {
  color: #2c3e50;
  font-family: Source Sans Pro, Helvetica, sans-serif;
  text-align: center;
  flex-grow: 1;
}

</style>
