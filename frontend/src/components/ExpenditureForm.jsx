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
    <form onSubmit={handleSubmit} className="expenditure-form">
        <div className="form-group">
            <label htmlFor="name">支出名</label>
            <input
                id="name"
                type="text"
                value={name}
                onChange={(event) => setName(event.target.value)}
                placeholder="例：スーパーで買い物"
                aria-invalid={Boolean(errors.name)}
            />
            {errors.name && (
                <p className="field-error">{errors.name}</p>
            )}
        </div>

        <div className="form-group">
            <label htmlFor="amount">金額</label>
            <div className="amount-input">
                <input
                    id="amount"
                    type="number"
                    value={amount}
                    onChange={(event) => setAmount(event.target.value)}
                    placeholder="例：3000"
                    aria-invalid={Boolean(errors.amount)}
                />
                <span>円</span>
            </div>
            {errors.amount && (
                <p className="field-error">{errors.amount}</p>
            )}
        </div>

        <div className="form-group">
            <label htmlFor="expenditureDate">支出日</label>
            <input
                id="expenditureDate"
                type="date"
                value={expenditureDate}
                onChange={(event) => setExpenditureDate(event.target.value)}
                aria-invalid={Boolean(errors.expenditureDate)}
            />
            {errors.expenditureDate && (
                <p className="field-error">{errors.expenditureDate}</p>
            )}
        </div>

        <div className="form-group">
            <label htmlFor="categoryId">カテゴリ</label>
            <select
                id="categoryId"
                value={categoryId}
                onChange={(event) => setCategoryId(event.target.value)}
                aria-invalid={Boolean(errors.categoryId)}
            >
                <option value="">選択してください</option>
                {categories.map((category) => (
                    <option key={category.id} value={category.id}>
                        {category.categoryName}
                    </option>
                ))}
            </select>
            {errors.categoryId && (
                <p className="field-error">{errors.categoryId}</p>
            )}
        </div>

        <div className="form-group">
            <label htmlFor="paymentMethodId">支払い方法</label>
            <select
                id="paymentMethodId"
                value={paymentMethodId}
                onChange={(event) => setPaymentMethodId(event.target.value)}
                aria-invalid={Boolean(errors.paymentMethodId)}
            >
                <option value="">選択してください</option>
                {paymentMethods.map((paymentMethod) => (
                    <option key={paymentMethod.id} value={paymentMethod.id}>
                        {paymentMethod.paymentMethodName}
                    </option>
                ))}
            </select>
            {errors.paymentMethodId && (
                <p className="field-error">{errors.paymentMethodId}</p>
            )}
        </div>

        <div className="form-group form-group-full">
            <label htmlFor="memo">メモ</label>
            <textarea
                id="memo"
                value={memo}
                onChange={(event) => setMemo(event.target.value)}
                placeholder="必要に応じてメモを入力"
                rows={3}
                aria-invalid={Boolean(errors.memo)}
            />
            {errors.memo && (
                <p className="field-error">{errors.memo}</p>
            )}
        </div>

        <div className="form-actions">
            <button type="submit" className="btn btn-primary">
                {editingExpenditure ? '更新する' : '登録する'}
            </button>
        </div>
    </form>
)
}

export default ExpenditureForm