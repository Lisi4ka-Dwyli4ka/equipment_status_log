import initSqlJs from 'sql.js'

let db

const SQL_CONFIG = {
  locateFile: (file) => `https://sql.js.org/dist/${file}`,
}

const saveDb = () => {
  try {
    if (!db) return
    const data = db.export()
    localStorage.setItem('sqlite_db', JSON.stringify(Array.from(data)))
  } catch (e) {
    console.error('Failed to save database:', e)
  }
}

const loadDb = () => {
  try {
    const savedDb = localStorage.getItem('sqlite_db')
    return savedDb ? new Uint8Array(JSON.parse(savedDb)) : null
  } catch (e) {
    console.error('Failed to load database:', e)
    return null
  }
}

const initDB = async () => {
  try {
    const SQL = await initSqlJs(SQL_CONFIG)
    db = new SQL.Database(loadDb())

    // Создаем таблицы, если их нет
    const tables = db.exec("SELECT name FROM sqlite_master WHERE type='table'")
    if (tables.length === 0) {
      db.exec(`
        CREATE TABLE teachers (
          id INTEGER PRIMARY KEY AUTOINCREMENT,
          lastname TEXT NOT NULL,
          firstname TEXT NOT NULL
        );
        
        CREATE TABLE auditorias (
          id INTEGER PRIMARY KEY AUTOINCREMENT,
          name TEXT NOT NULL
        );
        
        CREATE TABLE equipment (
          id INTEGER PRIMARY KEY AUTOINCREMENT,
          name TEXT NOT NULL,
          description TEXT
        );
        
        CREATE TABLE journal_requests (
          id INTEGER PRIMARY KEY AUTOINCREMENT,
          auditoria_id INTEGER,
          teacher_id INTEGER,
          equipment_id INTEGER,
          comment TEXT,
          date TEXT,
          FOREIGN KEY (teacher_id) REFERENCES teachers(id),
          FOREIGN KEY (auditoria_id) REFERENCES auditorias(id),
          FOREIGN KEY (equipment_id) REFERENCES equipment(id)
        );
        
        INSERT INTO teachers (lastname, firstname) VALUES 
          ('Вахонин', 'Николай Кириллович'),
          ('Литвинский', 'Игорь Евгеньевич'),
          ('Калинин', 'Максим Александрович'),
          ('Пархимович', 'Анна Вячеславовна'),
          ('Гардейчик', 'Сергей Михайлович');
            
        INSERT INTO auditorias (name) VALUES 
          ('72'),
          ('73'),
          ('62'),
          ('63'),
          ('52'),
          ('53');
            
        INSERT INTO equipment (name, description) VALUES 
          ('Стол', 'Компьютерный'),
          ('Стол', 'Преподовательский'),
          ('Кресло', '"Марис"'),
          ('Стул', '"ИЗО"'),
          ('Шторы', 'Кассетные'),
          ('Стол', 'Конференционный'),
          ('Стол', 'Ученический'),
          ('Доска', 'Маркерная'),
          ('Монитор', ''),
          ('Системный блок', ''),
          ('Клавиатура', ''),
          ('Компьютерная мышь', ''),
          ('Проектор', '');
      `)
      saveDb()
    }

    return true
  } catch (error) {
    console.error('Database initialization failed:', error)
    return false
  }
}

const executeQuery = async (query, params = []) => {
  if (!db) await initDB()
  try {
    const stmt = db.prepare(query)
    if (params.length > 0) stmt.bind(params)

    const result = []
    while (stmt.step()) {
      result.push(stmt.getAsObject())
    }
    stmt.free()
    return result
  } catch (error) {
    console.error('Query error:', error)
    throw error
  }
}

const executeUpdate = async (query, params = []) => {
  if (!db) await initDB()
  try {
    db.run(query, params)
    saveDb()
    return true
  } catch (error) {
    console.error('Update error:', error)
    throw error
  }
}

// Teachers
const getTeachers = async () => executeQuery('SELECT * FROM teachers ORDER BY lastname')
const addTeacher = async (lastname, firstname) =>
  executeUpdate('INSERT INTO teachers (lastname, firstname) VALUES (?, ?)', [lastname, firstname])
const deleteTeacher = async (id) => executeUpdate('DELETE FROM teachers WHERE id = ?', [id])

// Auditorias
const getAuditorias = async () => executeQuery('SELECT * FROM auditorias ORDER BY name')
const addAuditoria = async (name) =>
  executeUpdate('INSERT INTO auditorias (name) VALUES (?)', [name])
const deleteAuditoria = async (id) => executeUpdate('DELETE FROM auditorias WHERE id = ?', [id])

// Equipment
const getEquipment = async () => executeQuery('SELECT * FROM equipment ORDER BY name')
const addEquipment = async (name, description) =>
  executeUpdate('INSERT INTO equipment (name, description) VALUES (?, ?)', [
    name,
    description || '',
  ])
const deleteEquipment = async (id) => executeUpdate('DELETE FROM equipment WHERE id = ?', [id])

// Requests
const getRequests = async () =>
  executeQuery(`
  SELECT jr.id, jr.date, jr.comment, a.name as auditoria_name,
         t.lastname as teacher_lastname, t.firstname as teacher_firstname,
         e.name as equipment_name
  FROM journal_requests jr
  LEFT JOIN auditorias a ON jr.auditoria_id = a.id
  LEFT JOIN teachers t ON jr.teacher_id = t.id
  LEFT JOIN equipment e ON jr.equipment_id = e.id
  ORDER BY jr.date DESC
`)

const addRequest = async (data) =>
  executeUpdate(
    'INSERT INTO journal_requests (teacher_id, auditoria_id, equipment_id, comment, date) VALUES (?, ?, ?, ?, ?)',
    [
      data.teacher_id,
      data.auditoria_id,
      data.equipment_id,
      data.comment || '',
      data.date || new Date().toISOString(),
    ],
  )

const deleteRequest = async (id) => executeUpdate('DELETE FROM journal_requests WHERE id = ?', [id])

export {
  initDB,
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
  addRequest,
  deleteRequest,
}
