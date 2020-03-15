import React from 'react'

const Convert = (props) =>  {

    const {
        currencyOptions,
        selectedCurrency,
        onChangeCurrency,
        amount,
        onChangeAmount,
        showValidationError
    } = props;

    return (
        <div className="currency">
            <div className="input-group mb-3">
                <select value={selectedCurrency} onChange={onChangeCurrency} className="custom-select" >
                    {currencyOptions.map(option => (
                        <option key={option} value={option}>{option}</option>
                    ))}
                </select>
            </div>
            <div className="monetary">
                <input type="number" value={amount} onChange={onChangeAmount}/>
                {showValidationError && 
                <div style={{fontSize:12, color: "red"}}>Field can't be empty</div>}
            </div>
        </div>
    )
}

export default Convert
