<template>
  <n-config-provider :theme-overrides="themeOverrides">
    <n-layout class="blog-layout">
      <Header />
      <n-layout-content class="blog-main">
        <router-view />
      </n-layout-content>
      <Footer />
    </n-layout>
  </n-config-provider>
</template>

<script setup lang="ts">
import { type GlobalThemeOverrides } from 'naive-ui'
import Header from './Header.vue'
import Footer from './Footer.vue'

const themeOverrides: GlobalThemeOverrides = {
  common: {
    primaryColor: '#18a058',
    primaryColorHover: '#36ad6a',
    primaryColorPressed: '#0c7a43'
  }
}
</script>

<style scoped lang="scss">
.blog-layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background: #f5f7fa;

  .blog-main {
    flex: 1;
    max-width: 1200px;
    width: 100%;
    margin: 30px auto;
    padding: 0 20px;
  }
}
</style>
