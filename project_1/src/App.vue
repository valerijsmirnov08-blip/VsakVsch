<script setup>
import { RouterView } from 'vue-router';
import AppHeader from '@/components/AppHeader.vue';
import AppFooter from '@/components/AppFooter.vue';
import SupportChat from './components/SupportChat.vue';
import router from './router.ts';
import AppDeshBoardHeader from './components/AppDeshBoardHeader.vue';
</script>
<template>
  <div class="app-layout">
      <AppDeshBoardHeader v-if="$route.path === '/vendor_dash_board'"/>
      <AppHeader v-else/>
    <main class="main-content">
      <RouterView />
    </main>
      <AppFooter />
  </div>
  <SupportChat/>
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