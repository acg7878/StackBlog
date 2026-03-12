import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'

// 创建全局 Message、Dialog 等 API
import {
  create,
  NButton,
  NCard,
  NLayout,
  NLayoutHeader,
  NLayoutContent,
  NLayoutFooter,
  NMenu,
  NInput,
  NIcon,
  NGrid,
  NGridItem,
  NSpin,
  NSpace,
  NTag,
  NPagination,
  NEmpty,
  NDivider,
  NAlert,
  NText,
  NP,
  NUl,
  NList,
  NListItem,
  NDescriptions,
  NDescriptionsItem,
  NConfigProvider,
  NMessageProvider
} from 'naive-ui'

const naive = create({
  components: [
    NButton,
    NCard,
    NLayout,
    NLayoutHeader,
    NLayoutContent,
    NLayoutFooter,
    NMenu,
    NInput,
    NIcon,
    NGrid,
    NGridItem,
    NSpin,
    NSpace,
    NTag,
    NPagination,
    NEmpty,
    NDivider,
    NAlert,
    NText,
    NP,
    NUl,
    NList,
    NListItem,
    NDescriptions,
    NDescriptionsItem,
    NConfigProvider,
    NMessageProvider
  ]
})

const app = createApp(App)

app.use(createPinia())
app.use(router)
app.use(naive)

app.mount('#app')
