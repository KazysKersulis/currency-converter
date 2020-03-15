import React, {useState, useEffect, Fragment} from 'react';
import Convert from './Convert';
import './App.css';

// const BASE_URL = 'https://api.exchangeratesapi.io/latest'
const BASE_URL = 'http://127.0.0.1:8080/v1/currencies/rates'

function App() {

  const [currencyOptions, setCurrencyOptions] = useState([]);
  const [fromCurrency, setFromCurrency] = useState();
  const [toCurrency, setToCurrency] = useState();
  const [amount, setAmount] = useState(1);
  const [amountInFromCurrency, setAmountInFromCurrency] = useState(true);
  const [exchangeRate, setExchangeRate] = useState();
  const [showValidationError, setShowValidationError] = useState(false);

  let toAmount, fromAmount

  if (amountInFromCurrency) {
    fromAmount = amount
    toAmount = (amount * exchangeRate).toFixed(4)
  } else {
    toAmount = amount
    fromAmount = (amount / exchangeRate).toFixed(4)
  }

  useEffect(() => {
    fetch(`${BASE_URL}/EUR`)
      .then(res => res.json())
      .then(data => {
        const firstCurrency = Object.keys(data.rates)[0];
        setCurrencyOptions([data.base, ...Object.keys(data.rates)])
        setFromCurrency(data.base)
        setToCurrency(firstCurrency)
        setExchangeRate(data.rates[firstCurrency])
      })
      .catch(
        console.log("error")
      )
  }, [])

  useEffect(() => {
    if (fromCurrency != null && toCurrency != null) {
      fetch(`${BASE_URL}/${fromCurrency}`)
        .then(res => res.json())
        .then(data => setExchangeRate(data.rates[toCurrency]))
    }
  }, [fromCurrency, toCurrency])

  let primaryCurrency;
  primaryCurrency = <Convert 
    currencyOptions={currencyOptions}
    selectedCurrency={fromCurrency}
    onChangeCurrency={e => setFromCurrency(e.target.value)}
    amount={fromAmount}
    onChangeAmount={handleFromAmountChange}
    showValidationError={showValidationError}
    />

  let secondaryCurrency;
  secondaryCurrency = <Convert 
    currencyOptions={currencyOptions}
    selectedCurrency={toCurrency}
    onChangeCurrency={e => setToCurrency(e.target.value)}
    amount={toAmount}
    onChangeAmount={handleToAmountChange}
    showValidationError={showValidationError}
  />

  let calculatorComponent;
  calculatorComponent = (
    <Fragment >
      <div className="calculator">
        {primaryCurrency}
        {secondaryCurrency}
        {currencyOptions.length > 0 && <h1>1 {fromCurrency} = {exchangeRate} {toCurrency}</h1>}
      </div>
    </Fragment>
  )

  function handleFromAmountChange(e) {
    setAmountInFromCurrency(true)
    setShowValidationError(false)
    if (e.target.value === "") {
      setShowValidationError(true)
    } else {
      setAmount(e.target.value)
    }
  }

  function handleToAmountChange(e) {
    setAmount(e.target.value)
    setAmountInFromCurrency(false)

    if (e.target.value === "") {
      setShowValidationError(true)
    } else {
      setShowValidationError(false)
    }
  }

  return (
    <div className="App-container app-cont-center">

      <div className="header">
        <h1>Currency Converter</h1>
      </div>
      {calculatorComponent}
    </div>
  );
}

export default App;
