<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { initDB, getTeachers, getAuditorias, getEquipment, addRequest } from '../db/db.js'
const isAuthenticated = localStorage.getItem('journal_auth') === 'true'
const router = useRouter()

const form = ref({
  teacher_id: null,
  auditoria_id: null,
  equipment_id: null,
  comment: '',
})

const teachers = ref([])
const auditorias = ref([])
const equipmentList = ref([])

onMounted(async () => {
  await initDB()
  teachers.value = await getTeachers()
  auditorias.value = await getAuditorias()
  equipmentList.value = await getEquipment()
})

const submitForm = async () => {
  if (!form.value.teacher_id || !form.value.auditoria_id || !form.value.equipment_id) {
    alert('Заполните все обязательные поля!')
    return
  }

  await addRequest({
    teacher_id: form.value.teacher_id,
    auditoria_id: form.value.auditoria_id,
    equipment_id: form.value.equipment_id,
    comment: form.value.comment,
    date: new Date().toISOString(),
  })

  form.value = {
    teacher_id: null,
    auditoria_id: null,
    equipment_id: null,
    comment: '',
  }
  alert('Запись успешно добавлена!')
}
// Для отладки
const goToJournal = () => {
  console.log('Кнопка нажата!') 
  router
    .push('/journal')
    .then(() => console.log('Переход выполнен'))
    .catch((err) => console.error('Ошибка перехода:', err))
}
</script>

<template>
  <div class="max-w-md mx-auto my-5 p-6 bg-gray-50 rounded-lg shadow-sm">
    <h2 class="text-xl font-semibold text-gray-800 mb-6">Новая запись в журнале</h2>

    <div class="mb-4">
      <select 
        v-model="form.teacher_id" 
        required
        class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
      >
        <option :value="null">Выберите преподавателя</option>
        <option v-for="teacher in teachers" :key="teacher.id" :value="teacher.id">
          {{ teacher.lastname }} {{ teacher.firstname }}
        </option>
      </select>
    </div>

    <div class="mb-4">
      <select 
        v-model="form.auditoria_id" 
        required
        class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
      >
        <option :value="null">Выберите аудиторию</option>
        <option v-for="auditoria in auditorias" :key="auditoria.id" :value="auditoria.id">
          {{ auditoria.name }}
        </option>
      </select>
    </div>

    <div class="mb-4">
      <select 
        v-model="form.equipment_id" 
        required
        class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
      >
        <option :value="null">Выберите оборудование</option>
        <option v-for="item in equipmentList" :key="item.id" :value="item.id">
          {{ item.name }} ({{ item.description }})
        </option>
      </select>
    </div>

    <div class="mb-4">
      <textarea 
        v-model="form.comment" 
        placeholder="Комментарий"
        class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 min-h-[80px]"
      ></textarea>
    </div>

    <div class="flex gap-3">
      <button 
        @click="submitForm"
        class="flex-1 px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
      >
        Добавить запись
      </button>
      <button 
        @click="goToJournal" 
        class="flex-1 px-4 py-2 bg-gray-800 text-white rounded-md hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-gray-500 focus:ring-offset-2"
      >
        Перейти в журнал
      </button>
    </div>
  </div>
</template>

<style scoped>

</style>
