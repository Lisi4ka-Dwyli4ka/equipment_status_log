import { createRouter, createWebHistory } from 'vue-router'
import FormComponent from '@/components/FormComponent.vue'
import JournalPage from '@/components/JournalPage.vue'

const routes = [
  {
    path: '/',
    name: 'form',
    component: FormComponent,
  },
  {
    path: '/journal',
    name: 'journal',
    component: JournalPage,
    meta: { requiresAuth: true },
  },
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
})

export default router
