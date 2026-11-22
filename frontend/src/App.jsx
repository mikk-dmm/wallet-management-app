import { useEffect, useState } from 'react'
import ExpenditureList from './components/ExpenditureList'
import ExpenditureForm from './components/ExpenditureForm'
import './App.css'

function App() {
  const [expenditures, setExpenditures] = useState([])
  const [categories, setCategories] = useState([])
  const [paymentMethods, setPaymentMethods] = useState([])
  const [showForm, setShowForm] = useState(false)
  const [csrf, setCsrf] =useState(null)
  const [editingExpenditure, setEditingExpenditure] = useState(null)
  const [isLoggingOut, setIsLoggingOut] = useState(false)

  const fetchExpenditure = () => {
    fetch('/api/expenditures')
      .then(response => {
        if (response.status == 401) {
          window.location.href = '/login';
          return null;
        }
        return response.json();
      })
      .then(data => {
        if(data == null) return;
        setExpenditures(data);
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

const handleLogout = async () => {
  if (isLoggingOut) return

  if (!csrf?.headerName || !csrf?.token) {
      console.error('CSRF情報が取得できていません')
      return
  }

  setIsLoggingOut(true)

  try {
      const response = await fetch('/logout', {
          method: 'POST',
          headers: {
              [csrf.headerName]: csrf.token
          },
          credentials: 'same-origin'
      })

      if (!response.ok) {
          console.error('ログアウトに失敗しました:', response.status)
          return
      }

      window.location.assign('/login')
  } catch (error) {
      console.error('ログアウト通信に失敗しました:', error)
  } finally {
      setIsLoggingOut(false)
  }
}
  return (
    <div className="expenditure-page">
      <header className="app-header">
        <div className="app-header-inner">
          <div className="app-header-top">
            <a href="/dashboard" className="app-brand">
                Wallet Management
            </a>

            <button
              type="button"
              className="btn btn-secondary"
              onClick={handleLogout}
              disabled={!csrf || isLoggingOut}
            >
              {isLoggingOut ? 'ログアウト中...' : 'ログアウト'}
            </button>
          </div>

        <nav className="app-nav" aria-label="メインナビゲーション">
            <a href="/dashboard">ダッシュボード</a>
            <a href="/expenditures" className="active" aria-current="page">
                支出管理
            </a>
            <a href="/budgets/monthlyBudget">月間予算</a>
            <a href="/budgets/categoryBudget">カテゴリ別予算</a>
            <a href="/categories">カテゴリ</a>
            <a href="/paymentMethods">支払方法</a>
          </nav>
        </div>
    </header>

      <main className="expenditure-container">
        <div className="expenditure-page-header">
          <div>
            <h1>支出管理</h1>
            <p>日々の支出を記録・管理します。</p>
          </div>

          {!showForm && (
            <button
              type="button"
              className="btn btn-primary"
              onClick={handleCreate}
            >
              ＋ 新規支出登録
            </button>
          )}
        </div>

        {showForm && (
          <section className="card expenditure-form-card">
            <div className="expenditure-section-header">
              <h2>
                {editingExpenditure ? '支出の編集' : '支出の登録'}
              </h2>

              <button
                type="button"
                className="btn btn-outline"
                onClick={handleClose}
              >
                閉じる
              </button>
            </div>

            <ExpenditureForm
              categories={categories}
              paymentMethods={paymentMethods}
              onSaved={handleSaved}
              csrf={csrf}
              editingExpenditure={editingExpenditure}
            />
          </section>
        )}

        <ExpenditureList
          expenditures={expenditures}
          onEdit={handleEdit}
          onDelete={handleDelete}
        />
      </main>
    </div>
  )

}
export default App
