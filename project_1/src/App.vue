<script setup>
import { RouterView } from 'vue-router';
import AppHeader from '@/components/AppHeader.vue';
import AppFooter from '@/components/AppFooter.vue';
</script>
<template>
  <div class="app-layout">
      <AppHeader />
    <main class="main-content">
      <RouterView />
    </main>
      <AppFooter />
  </div>
</template>
<style scoped>
.app-layout {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.main-content {
  flex: 1;
}
</style>