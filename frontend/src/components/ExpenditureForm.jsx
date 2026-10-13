import { useEffect, useState } from 'react'

function ExpenditureForm({
    categories,
    paymentMethods,
    onSaved,
    csrf,
    editingExpenditure
}) {

const [name, setName] = useState('')
const [amount, setAmount] = useState('')
const [expenditureDate, setExpenditureDate] = useState('')
const [categoryId, setCategoryId] = useState('')
const [paymentMethodId, setPaymentMethodId] = useState('')
const [memo, setMemo] = useState('')
const [errors, setErrors] = useState({})
useEffect(() => {
    if (editingExpenditure) {
        setName(editingExpenditure.name)
        setAmount(String(editingExpenditure.amount))
        setExpenditureDate(editingExpenditure.expenditureDate)
        setCategoryId(String(editingExpenditure.categoryId))
        setPaymentMethodId(String(editingExpenditure.paymentMethodId))
        setMemo(editingExpenditure.memo ?? '')
    } else {
        setAmount('')
        setExpenditureDate('')
        setCategoryId('')
        setPaymentMethodId('')
        setMemo('')
        setName('')
    }
    setErrors({})
}, [editingExpenditure])


const handleSubmit = async (event) => {
    event.preventDefault()
    
    const isEditing = editingExpenditure != null
    const url = isEditing
        ? `/api/expenditures/${editingExpenditure.id}`
        : '/api/expenditures'

    const method = isEditing
        ? 'PUT'
        : 'POST'

    const requestData = {
        name,
        amount: amount === '' ? null : Number(amount),
        expenditureDate,
        categoryId: categoryId === '' ? null : Number(categoryId),
        paymentMethodId: paymentMethodId === '' ? null : Number(paymentMethodId),
        memo
    }

    if (!csrf) {
        console.error('CSRF tokenが無効です')
        return
    }


    const response = await fetch(url, {
        method: method,
        headers: {
            'Content-Type': 'application/json',
            [csrf.headerName]: csrf.token
        },
        body: JSON.stringify(requestData)
    })

    if (!response.ok) {
        
        if (response.status === 400) {
            const errorData = await response.json()
            setErrors(errorData)
        }

        console.error('保存失敗', response.status)
        return
    }

    setErrors({})

    onSaved()
}

return (
    <form onSubmit={handleSubmit}>

    <div>
        <label>名前</label>
        <input
            type="text"
            value={name}
            onChange={(event) => setName(event.target.value)}
        />

        {errors.name && (
            <p>{errors.name}</p>
        )}
    </div>


    <div>
        <label>金額</label>
        <input
            type="number"
            value={amount}
            onChange={(event) => setAmount(event.target.value)}
        />

        {errors.amount && (
            <p>{errors.amount}</p>
        )}
    </div>


    <div>
        <label>支出日</label>
        <input
            type="date"
            value={expenditureDate}
            onChange={(event) => setExpenditureDate(event.target.value)}
        />

        {errors.expenditureDate && (
            <p>{errors.expenditureDate}</p>
        )}
    </div>


    <div>
        <label>カテゴリ</label>

        <select
            value={categoryId}
            onChange={(event) => setCategoryId(event.target.value)}
        >
        <option value="">
            選択してください
        </option>

        {categories.map((category) => (
            <option
                key={category.id}
                value={category.id}
            >
            {category.categoryName}
            </option>
        ))}
        </select>

        {errors.categoryId && (
            <p>{errors.categoryId}</p>
        )}
    </div>


    <div>
        <label>支払い方法</label>

        <select
            value={paymentMethodId}
            onChange={(event) => setPaymentMethodId(event.target.value)}
        >
        <option value="">
            選択してください
        </option>

        {paymentMethods.map((paymentMethod) => (
            <option
                key={paymentMethod.id}
                value={paymentMethod.id}
            >
            {paymentMethod.paymentMethodName}
            </option>
        ))}
        </select>

        {errors.paymentMethodId && (
            <p>{errors.paymentMethodId}</p>
        )}
    </div>


    <div>
        <label>メモ</label>

        <textarea
            value={memo}
            onChange={(event) => setMemo(event.target.value)}
        />

        {errors.memo && (
            <p>{errors.memo}</p>
        )}
    </div>


    <button type="submit">
        {editingExpenditure ? '更新' : '登録'}
    </button>

    </form>
)
}

export default ExpenditureForm