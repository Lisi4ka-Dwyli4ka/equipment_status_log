const API_URL = 'http://localhost:8080/api'

export const getTeachers = async () => {
  const response = await fetch(`${API_URL}/teachers`)
  return await response.json()
}

export const addTeacher = async (lastname, firstname) => {
  const response = await fetch(`${API_URL}/teachers`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
      lastName: lastname,
      firstName: firstname,
    }),
  })
  return await response.json()
}

export const addRequest = async (requestData) => {
  try {
    const response = await fetch(`${API_URL}/requests`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        teacher: { id: requestData.teacherId },
        auditoria: { id: requestData.auditoriaId },
        equipment: { id: requestData.equipmentId },
        comment: requestData.comment,
        date: requestData.date,
      }),
    })

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`)
    }

    return await response.json() // Ожидаем JSON-ответ
  } catch (error) {
    console.error('Failed to add request:', error)
    throw new Error('Failed to add request: ' + error.message)
  }
}

export const deleteTeacher = async (id) => {
  await fetch(`${API_URL}/teachers/${id}`, {
    method: 'DELETE',
  })
}

export const getAuditorias = async () => {
  const response = await fetch(`${API_URL}/auditorias`)
  return await response.json()
}

export const addAuditoria = async (name) => {
  const response = await fetch(`${API_URL}/auditorias`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ name }),
  })
  return await response.json()
}

export const deleteAuditoria = async (id) => {
  await fetch(`${API_URL}/auditorias/${id}`, {
    method: 'DELETE',
  })
}

export const getEquipment = async () => {
  const response = await fetch(`${API_URL}/equipment`)
  return await response.json()
}

export const addEquipment = async (name, description) => {
  const response = await fetch(`${API_URL}/equipment`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
      name,
      description: description || null,
    }),
  })
  return await response.json()
}

export const deleteEquipment = async (id) => {
  await fetch(`${API_URL}/equipment/${id}`, {
    method: 'DELETE',
  })
}

export const getRequests = async () => {
  const response = await fetch(`${API_URL}/requests`)
  const requests = await response.json()
  return requests.map((request) => ({
    id: request.id,
    date: request.date,
    comment: request.comment,
    teacher_lastname: request.teacher.lastName,
    teacher_firstname: request.teacher.firstName,
    auditoria_name: request.auditoria.name,
    equipment_name: request.equipment.name,
  }))
}

export const deleteRequest = async (id) => {
  await fetch(`${API_URL}/requests/${id}`, {
    method: 'DELETE',
  })
}

export const checkAuth = async () => {
  return true // Простая заглушка для проверки авторизации
}

export const logout = async () => {
  // Заглушка для выхода
}
