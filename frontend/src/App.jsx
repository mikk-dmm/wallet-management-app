import { useEffect, useState } from 'react'
import ExpenditureList from './components/ExpenditureList'
import ExpenditureForm from './components/ExpenditureForm'

function App() {
  const [expenditures, setExpenditures] = useState([])
  const [categories, setCategories] = useState([])
  const [paymentMethods, setPaymentMethods] = useState([])
  const [showForm, setShowForm] = useState(false)
  const [csrf, setCsrf] =useState(null)
  const [editingExpenditure, setEditingExpenditure] = useState(null)

  const fetchExpenditure = () => {
    fetch('/api/expenditures')
      .then(response => response.json())
      .then(data => {
        setExpenditures(data)
      })
  }

  useEffect(() => {
    fetchExpenditure()
  },[])

  const handleSaved = () => {
    fetchExpenditure()
    setShowForm(false)
    setEditingExpenditure(null)
  }

  const handleEdit = (expenditure) => {
    setEditingExpenditure(expenditure)
    setShowForm(true)
  }

  useEffect(() => {
    fetch('/api/categories')
    .then(response => {
      console.log("status:", response.status);
      return response.json();
    })
    .then(data => {
      console.log(data)
      setCategories(data)
    })
  },[])

  useEffect(() => {
    fetch('/api/payment-methods')
    .then(response => {
      console.log("status:", response.status);
      return response.json();
    })
    .then(data => {
      console.log(data)
      setPaymentMethods(data)
    })
  }, [])

  useEffect(() => {
    fetch('/api/csrf')
      .then(response => response.json())
      .then(data => {
        console.log('csrf:', data)
        setCsrf(data)
      })
  }, [])
  
  const handleCreate = () => {
    setEditingExpenditure(null)
    setShowForm(true)
  }
  
  const handleClose = () => {
    setShowForm(false)
    setEditingExpenditure(null)
  }

  const handleDelete = async (id) => {

  if (!csrf || !csrf.headerName || !csrf.token) {
    console.error('CSRF情報がありません')
    return
  }

  const response = await fetch(`/api/expenditures/${id}`, {
    method: 'DELETE',
    headers: {
      [csrf.headerName]: csrf.token
    }
  })

  if (!response.ok) {
    console.error('削除に失敗しました:', response.status)
    return
  }

  fetchExpenditure()
}
  return (
    <>
      <h1>Hello Expenditures</h1>
      <button onClick={handleCreate}>新規登録</button>
      <button onClick={handleClose}>閉じる</button>

      {showForm && (
        <ExpenditureForm
          categories={categories}
          paymentMethods={paymentMethods}
          onSaved={handleSaved}
          csrf={csrf}
          editingExpenditure={editingExpenditure}
        />
      )}

      <ExpenditureList expenditures={expenditures}
        onEdit={handleEdit}
        onDelete={handleDelete}
      />
      
    </>

  )

}
export default App
