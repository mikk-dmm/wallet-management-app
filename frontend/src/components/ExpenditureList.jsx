function ExpenditureList({ expenditures, onEdit, onDelete }) {
    const formatYen = (amount) =>
        new Intl.NumberFormat('ja-JP', {
            style: 'currency',
            currency: 'JPY',
            maximumFractionDigits: 0
        }).format(amount)

    return (
        <section className="card expenditure-list-card">
            <h2>支出一覧</h2>

            {expenditures.length === 0 ? (
                <p className="empty-message">
                    まだ支出が登録されていません。
                </p>
            ) : (
                <div className="table-scroll">
                    <table className="expenditure-table">
                        <thead>
                            <tr>
                                <th>支出日</th>
                                <th>支出名</th>
                                <th>カテゴリ</th>
                                <th>支払い方法</th>
                                <th className="amount-column">金額</th>
                                <th className="action-column">操作</th>
                            </tr>
                        </thead>

                        <tbody>
                            {expenditures.map((expenditure) => (
                                <tr key={expenditure.id}>
                                    <td>{expenditure.expenditureDate}</td>
                                    <td>{expenditure.name}</td>
                                    <td>{expenditure.categoryName}</td>
                                    <td>{expenditure.paymentMethodName}</td>
                                    <td className="amount-column">
                                        {formatYen(expenditure.amount)}
                                    </td>
                                    <td>
                                        <div className="table-actions">
                                            <button
                                                type="button"
                                                className="btn btn-outline btn-sm"
                                                onClick={() => onEdit(expenditure)}
                                            >
                                                編集
                                            </button>

                                            <button
                                                type="button"
                                                className="btn btn-danger-outline btn-sm"
                                                onClick={() => {
                                                    if (window.confirm('この支出を削除しますか？')) {
                                                        onDelete(expenditure.id)
                                                    }
                                                }}
                                            >
                                                削除
                                            </button>
                                        </div>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            )}
        </section>
    )
}

export default ExpenditureList