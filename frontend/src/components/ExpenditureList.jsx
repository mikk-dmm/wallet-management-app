function ExpenditureList({ expenditures, onEdit, onDelete }) {

    return (
        <>
            <h2>支出一覧</h2>

            <ul>
                {expenditures.map((expenditure) => (
                    <li key={expenditure.id}>
                        {expenditure.name}
                        {expenditure.amount}
                        {expenditure.categoryName}
                        {expenditure.paymentMethodName}
                        {expenditure.expenditureDate}
                        <button onClick={() => onEdit(expenditure)}>
                            編集
                        </button>
                        <button onClick={() => onDelete(expenditure.id)}>
                            削除
                        </button>
                    </li>
                    ))}
            </ul>
        </>
    )
}

export default ExpenditureList