<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import {
  getTeachers,
  addTeacher,
  deleteTeacher,
  getAuditorias,
  addAuditoria,
  deleteAuditoria,
  getEquipment,
  addEquipment,
  deleteEquipment,
  getRequests,
  deleteRequest,
  logout as apiLogout,
} from '../api/api.js'

const router = useRouter()

// Состояния (реактивные переменные)
const isAuthenticated = ref(localStorage.getItem('journal_auth') === 'true' || false)
const password = ref('')
const authError = ref('')
const ADMIN_PASSWORD = '1111'

const entries = ref([])
const teachers = ref([])
const auditorias = ref([])
const equipmentList = ref([])

const newTeacher = ref({ lastname: '', firstname: '' })
const newAuditoria = ref({ name: '' })
const newEquipment = ref({ name: '', description: '' })

const activeTab = ref('journal') // 'journal', 'teachers', 'auditorias', 'equipment'

const entryColors = ref({})

// Методы

const loadColorsFromCookies = () => {
  const colorsCookie = document.cookie.split('; ').find((row) => row.startsWith('entryColors='))

  if (colorsCookie) {
    const colorsJson = decodeURIComponent(colorsCookie.split('=')[1])
    entryColors.value = JSON.parse(colorsJson)
  }
}

const saveColorsToCookies = () => {
  const colorsJson = JSON.stringify(entryColors.value)
  document.cookie = `entryColors=${encodeURIComponent(colorsJson)}; path=/; max-age=${60 * 60 * 24 * 30}` // 30 дней
}

const setEntryColor = (id, color) => {
  entryColors.value = {
    ...entryColors.value,
    [id]: color,
  }
  saveColorsToCookies()
}

const resetEntryColor = (id) => {
  const newColors = { ...entryColors.value }
  delete newColors[id]
  entryColors.value = newColors
  saveColorsToCookies()
}
// Добавляем новый метод для установки статуса записи
const setEntryStatus = (id, status) => {
  const color = status === 'APPROVED' ? 'green' : 'red'
  setEntryColor(id, color)
}

const loadData = async () => {
  try {
    const [requests, teachersData, auditoriasData, equipmentData] = await Promise.all([
      getRequests(),
      getTeachers(),
      getAuditorias(),
      getEquipment(),
    ])

    entries.value = requests
    teachers.value = teachersData
    auditorias.value = auditoriasData
    equipmentList.value = equipmentData
  } catch (error) {
    console.error('Ошибка загрузки:', error)
  }
}

const handleDeleteRequest = async (id) => {
  if (confirm('Удалить эту запись?')) {
    try {
      await deleteRequest(id)
      entries.value = await getRequests()
      resetEntryColor(id)
    } catch (error) {
      console.error('Ошибка удаления:', error)
      authError.value = 'Ошибка при удалении'
    }
  }
}

const checkPassword = () => {
  if (password.value === ADMIN_PASSWORD) {
    isAuthenticated.value = true
    authError.value = ''
    localStorage.setItem('journal_auth', 'true')
    loadData()
    loadColorsFromCookies()
  } else {
    authError.value = 'Неверный пароль!'
    password.value = ''
  }
}

const logout = () => {
  isAuthenticated.value = false
  password.value = ''
  localStorage.removeItem('journal_auth')
}

const goBack = () => {
  router.push('/')
}

// Добавление преподавателя
const handleAddTeacher = async () => {
  if (newTeacher.value.lastname && newTeacher.value.firstname) {
    try {
      await addTeacher(newTeacher.value.lastname, newTeacher.value.firstname)
      teachers.value = await getTeachers()
      newTeacher.value = { lastname: '', firstname: '' }
    } catch (error) {
      console.error('Ошибка при добавлении преподавателя:', error)
      authError.value = 'Не удалось добавить преподавателя'
    }
  }
}

// Удаление преподавателя
const handleDeleteTeacher = async (id) => {
  if (confirm('Удалить этого преподавателя?')) {
    try {
      await deleteTeacher(id)
      teachers.value = await getTeachers()
    } catch (error) {
      console.error('Ошибка при удалении преподавателя:', error)
      authError.value = 'Не удалось удалить преподавателя'
    }
  }
}

const handleAddAuditoria = async () => {
  if (newAuditoria.value.name) {
    try {
      await addAuditoria(newAuditoria.value.name)
      auditorias.value = await getAuditorias()
      newAuditoria.value = { name: '' }
    } catch (error) {
      console.error('Ошибка при добавлении аудитории:', error)
      authError.value = 'Не удалось добавить аудиторию'
    }
  }
}

const handleDeleteAuditoria = async (id) => {
  if (confirm('Удалить эту аудиторию?')) {
    try {
      await deleteAuditoria(id)
      auditorias.value = await getAuditorias()
    } catch (error) {
      console.error('Ошибка при удалении аудитории:', error)
      authError.value = 'Не удалось удалить аудиторию'
    }
  }
}

const handleAddEquipment = async () => {
  if (newEquipment.value.name) {
    try {
      await addEquipment(newEquipment.value.name, newEquipment.value.description)
      equipmentList.value = await getEquipment()
      newEquipment.value = { name: '', description: '' }
    } catch (error) {
      console.error('Ошибка при добавлении оборудования:', error)
      authError.value = 'Не удалось добавить оборудование'
    }
  }
}

const handleDeleteEquipment = async (id) => {
  if (confirm('Удалить это оборудование?')) {
    try {
      await deleteEquipment(id)
      equipmentList.value = await getEquipment()
    } catch (error) {
      console.error('Ошибка при удалении оборудования:', error)
      authError.value = 'Не удалось удалить оборудование'
    }
  }
}

// Загрузка данных при монтировании
 onMounted(() => {
   if (isAuthenticated.value) {
     loadData()
     loadColorsFromCookies()
   }
 });
</script>

<template>
  <div class="min-h-screen bg-gray-50 font-sans text-gray-800">
    <!-- Auth Screen -->
    <div
      v-if="!isAuthenticated"
      class="flex items-center justify-center min-h-screen bg-gradient-to-br from-blue-600 to-blue-800"
    >
      <div class="w-full max-w-md p-10 bg-white rounded-xl shadow-xl">
        <div class="flex justify-center mb-6 text-blue-500">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            width="48"
            height="48"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2"
            stroke-linecap="round"
            stroke-linejoin="round"
          >
            <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path>
            <polyline points="14 2 14 8 20 8"></polyline>
            <line x1="16" y1="13" x2="8" y2="13"></line>
            <line x1="16" y1="17" x2="8" y2="17"></line>
            <polyline points="10 9 9 9 8 9"></polyline>
          </svg>
        </div>
        <h2 class="text-2xl font-semibold text-center text-gray-800 mb-6">Вход в журнал</h2>
        <div class="space-y-4">
          <input
            v-model="password"
            type="password"
            placeholder="Введите пароль"
            @keyup.enter="checkPassword"
            class="w-full px-4 py-3 border border-gray-300 rounded-lg shadow-sm focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition"
          />
          <button
            @click="checkPassword"
            :disabled="!password"
            class="w-full px-4 py-3 bg-blue-600 text-white font-medium rounded-lg hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 disabled:bg-gray-300 disabled:cursor-not-allowed transition"
          >
            Войти
          </button>
        </div>
        <p v-if="authError" class="mt-4 text-red-600 text-center">{{ authError }}</p>
      </div>
    </div>

    <!-- Journal Content -->
    <div v-else class="min-h-screen">
      <!-- Header -->
      <div class="flex items-center justify-between p-6 bg-white shadow-sm">
        <button
          @click="goBack"
          class="flex items-center gap-2 px-4 py-2 text-gray-700 border border-gray-300 rounded-md hover:bg-gray-50 transition"
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            width="20"
            height="20"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2"
            stroke-linecap="round"
            stroke-linejoin="round"
          >
            <line x1="19" y1="12" x2="5" y2="12"></line>
            <polyline points="12 19 5 12 12 5"></polyline>
          </svg>
          На главную
        </button>
        <button
          @click="logout"
          class="px-4 py-2 text-red-600 border border-red-200 rounded-md hover:bg-red-50 transition"
        >
          Выйти
        </button>
      </div>

      <!-- Title -->
      <div class="px-8 pt-6">
        <h2 class="flex items-center gap-3 text-2xl font-semibold text-gray-800">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            width="28"
            height="28"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2"
            stroke-linecap="round"
            stroke-linejoin="round"
            class="text-blue-500"
          >
            <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path>
            <polyline points="14 2 14 8 20 8"></polyline>
            <line x1="16" y1="13" x2="8" y2="13"></line>
            <line x1="16" y1="17" x2="8" y2="17"></line>
            <polyline points="10 9 9 9 8 9"></polyline>
          </svg>
          Журнал записей
        </h2>
      </div>

      <!-- Tabs -->
      <div class="flex gap-2 px-8 mt-6 border-b border-gray-200">
        <button
          @click="activeTab = 'journal'"
          :class="{
            'text-blue-600 border-b-2 border-blue-600 font-medium': activeTab === 'journal',
            'text-gray-600 hover:text-gray-800': activeTab !== 'journal',
          }"
          class="px-4 py-2 text-sm focus:outline-none transition"
        >
          Журнал
        </button>
        <button
          @click="activeTab = 'teachers'"
          :class="{
            'text-blue-600 border-b-2 border-blue-600 font-medium': activeTab === 'teachers',
            'text-gray-600 hover:text-gray-800': activeTab !== 'teachers',
          }"
          class="px-4 py-2 text-sm focus:outline-none transition"
        >
          Преподаватели
        </button>
        <button
          @click="activeTab = 'auditorias'"
          :class="{
            'text-blue-600 border-b-2 border-blue-600 font-medium': activeTab === 'auditorias',
            'text-gray-600 hover:text-gray-800': activeTab !== 'auditorias',
          }"
          class="px-4 py-2 text-sm focus:outline-none transition"
        >
          Аудитории
        </button>
        <button
          @click="activeTab = 'equipment'"
          :class="{
            'text-blue-600 border-b-2 border-blue-600 font-medium': activeTab === 'equipment',
            'text-gray-600 hover:text-gray-800': activeTab !== 'equipment',
          }"
          class="px-4 py-2 text-sm focus:outline-none transition"
        >
          Оборудование
        </button>
      </div>

      <!-- Tab Content -->
      <div class="p-8">
        <!-- Journal Tab -->
        <div v-if="activeTab === 'journal'">
    <div class="overflow-x-auto bg-white rounded-lg shadow">
      <table class="min-w-full divide-y divide-gray-200">
              <thead class="bg-gray-50">
                <tr>
                  <th
                    class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                  >
                    Дата
                  </th>
                  <th
                    class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                  >
                    Аудитория
                  </th>
                  <th
                    class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                  >
                    Преподаватель
                  </th>
                  <th
                    class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                  >
                    Оборудование
                  </th>
                  <th
                    class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                  >
                    Комментарий
                  </th>
                  <th
                    class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                  >
                    Действия
                  </th>
                </tr>
              </thead>
               <tbody class="bg-white divide-y divide-gray-200">
          <tr
            v-for="entry in entries"
            :key="entry.id"
            class="transition-colors duration-300"
            :class="{
              'bg-green-50 hover:bg-green-100': entryColors[entry.id] === 'green',
              'bg-red-50 hover:bg-red-100': entryColors[entry.id] === 'red',
              'hover:bg-gray-50': !entryColors[entry.id],
            }"
          >
                  <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-700">
                    {{ new Date(entry.date).toLocaleString() }}
                  </td>
                  <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-700">
                    {{ entry.auditoria_name }}
                  </td>
                  <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-700">
                    {{ entry.teacher_lastname }} {{ entry.teacher_firstname }}
                  </td>
                  <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-700">
                    {{ entry.equipment_name }}
                  </td>
                  <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-700">
                    {{ entry.comment || '-' }}
                  </td>
                  <td class="px-6 py-4 whitespace-nowrap text-sm font-medium space-x-2">
                    <!-- Зеленая кнопка "Одобрено" -->
                    <button
                      @click="setEntryStatus(entry.id, 'APPROVED')"
                      class="p-2 bg-green-500 text-white rounded-full hover:bg-green-600 transition-colors duration-200 shadow hover:shadow-md"
                      title="Одобрить"
                    >
                      <svg
                        xmlns="http://www.w3.org/2000/svg"
                        width="16"
                        height="16"
                        viewBox="0 0 24 24"
                        fill="none"
                        stroke="currentColor"
                        stroke-width="2"
                        stroke-linecap="round"
                        stroke-linejoin="round"
                      >
                        <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path>
                        <polyline points="22 4 12 14.01 9 11.01"></polyline>
                      </svg>
                    </button>

                    <!-- Красная кнопка "Критично" -->
                    <button
                      @click="setEntryStatus(entry.id, 'REJECTED')"
                      class="p-2 bg-red-500 text-white rounded-full hover:bg-red-600 transition-colors duration-200 shadow hover:shadow-md"
                      title="Пометить как критично"
                    >
                      <svg
                        xmlns="http://www.w3.org/2000/svg"
                        width="16"
                        height="16"
                        viewBox="0 0 24 24"
                        fill="none"
                        stroke="currentColor"
                        stroke-width="2"
                        stroke-linecap="round"
                        stroke-linejoin="round"
                      >
                        <circle cx="12" cy="12" r="10"></circle>
                        <line x1="4.93" y1="4.93" x2="19.07" y2="19.07"></line>
                      </svg>
                    </button>

                    <!-- Кнопка удаления -->
                    <button
                      @click="handleDeleteRequest(entry.id)"
                      class="p-2 text-red-600 bg-red-100 rounded-full hover:bg-red-200 transition-colors duration-200 shadow hover:shadow-md"
                      title="Удалить запись"
                    >
                      <svg
                        xmlns="http://www.w3.org/2000/svg"
                        width="16"
                        height="16"
                        viewBox="0 0 24 24"
                        fill="none"
                        stroke="currentColor"
                        stroke-width="2"
                        stroke-linecap="round"
                        stroke-linejoin="round"
                      >
                        <polyline points="3 6 5 6 21 6"></polyline>
                        <path
                          d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"
                        ></path>
                      </svg>
                    </button>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
        <!-- Teachers Tab -->
        <div v-if="activeTab === 'teachers'">
          <div class="bg-gray-50 p-6 rounded-lg mb-6">
            <h3 class="text-lg font-medium text-gray-800 mb-4">Добавить преподавателя</h3>
            <div class="space-y-4">
              <input
                v-model="newTeacher.lastname"
                placeholder="Фамилия"
                class="w-full px-4 py-2 border border-gray-300 rounded-md focus:ring-blue-500 focus:border-blue-500"
              />
              <input
                v-model="newTeacher.firstname"
                placeholder="Имя"
                class="w-full px-4 py-2 border border-gray-300 rounded-md focus:ring-blue-500 focus:border-blue-500"
              />
              <button
                @click="handleAddTeacher"
                class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                Добавить
              </button>
            </div>
          </div>

          <div>
            <h3 class="text-lg font-medium text-gray-800 mb-4">Список преподавателей</h3>
            <div class="overflow-x-auto bg-white rounded-lg shadow">
              <table class="min-w-full divide-y divide-gray-200">
                <thead class="bg-gray-50">
                  <tr>
                    <th
                      class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                    >
                      Фамилия
                    </th>
                    <th
                      class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                    >
                      Имя
                    </th>
                    <th
                      class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                    >
                      Действия
                    </th>
                  </tr>
                </thead>
                <tbody class="bg-white divide-y divide-gray-200">
                  <tr v-for="teacher in teachers" :key="teacher.id" class="hover:bg-gray-50">
                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-700">
                      {{ teacher.lastName }}
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-700">
                      {{ teacher.firstName }}
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                      <button
                        @click="handleDeleteTeacher(teacher.id)"
                        class="text-red-600 hover:text-red-900 hover:bg-red-50 px-2 py-1 rounded"
                      >
                        Удалить
                      </button>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>

        <!-- Auditorias Tab -->
        <div v-if="activeTab === 'auditorias'">
          <div class="bg-gray-50 p-6 rounded-lg mb-6">
            <h3 class="text-lg font-medium text-gray-800 mb-4">Добавить аудиторию</h3>
            <div class="flex gap-4">
              <input
                v-model="newAuditoria.name"
                placeholder="Название аудитории"
                class="flex-1 px-4 py-2 border border-gray-300 rounded-md focus:ring-blue-500 focus:border-blue-500"
              />
              <button
                @click="handleAddAuditoria"
                class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                Добавить
              </button>
            </div>
          </div>

          <div>
            <h3 class="text-lg font-medium text-gray-800 mb-4">Список аудиторий</h3>
            <div class="overflow-x-auto bg-white rounded-lg shadow">
              <table class="min-w-full divide-y divide-gray-200">
                <thead class="bg-gray-50">
                  <tr>
                    <th
                      class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                    >
                      Название
                    </th>
                    <th
                      class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                    >
                      Действия
                    </th>
                  </tr>
                </thead>
                <tbody class="bg-white divide-y divide-gray-200">
                  <tr v-for="auditoria in auditorias" :key="auditoria.id" class="hover:bg-gray-50">
                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-700">
                      {{ auditoria.name }}
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                      <button
                        @click="handleDeleteAuditoria(auditoria.id)"
                        class="text-red-600 hover:text-red-900 hover:bg-red-50 px-2 py-1 rounded"
                      >
                        Удалить
                      </button>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>

        <!-- Equipment Tab -->
        <div v-if="activeTab === 'equipment'">
          <div class="bg-gray-50 p-6 rounded-lg mb-6">
            <h3 class="text-lg font-medium text-gray-800 mb-4">Добавить оборудование</h3>
            <div class="space-y-4">
              <input
                v-model="newEquipment.name"
                placeholder="Название"
                class="w-full px-4 py-2 border border-gray-300 rounded-md focus:ring-blue-500 focus:border-blue-500"
              />
              <textarea
                v-model="newEquipment.description"
                placeholder="Описание"
                class="w-full px-4 py-2 border border-gray-300 rounded-md focus:ring-blue-500 focus:border-blue-500 min-h-[100px]"
              ></textarea>
              <button
                @click="handleAddEquipment"
                class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                Добавить
              </button>
            </div>
          </div>

          <div>
            <h3 class="text-lg font-medium text-gray-800 mb-4">Список оборудования</h3>
            <div class="overflow-x-auto bg-white rounded-lg shadow">
              <table class="min-w-full divide-y divide-gray-200">
                <thead class="bg-gray-50">
                  <tr>
                    <th
                      class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                    >
                      Название
                    </th>
                    <th
                      class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                    >
                      Описание
                    </th>
                    <th
                      class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                    >
                      Действия
                    </th>
                  </tr>
                </thead>
                <tbody class="bg-white divide-y divide-gray-200">
                  <tr v-for="item in equipmentList" :key="item.id" class="hover:bg-gray-50">
                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-700">
                      {{ item.name }}
                    </td>
                    <td class="px-6 py-4 text-sm text-gray-700">{{ item.description || '-' }}</td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                      <button
                        @click="handleDeleteEquipment(item.id)"
                        class="text-red-600 hover:text-red-900 hover:bg-red-50 px-2 py-1 rounded"
                      >
                        Удалить
                      </button>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
button {
  transition: all 0.2s ease;
}
button:hover {
  transform: scale(1.05);
}
button svg {
  display: inline-block;
  vertical-align: middle;
}
</style>
