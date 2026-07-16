// import React, {useState, useEffect} from 'react';
// import './App.css';
//
// function App() {
//     const [invoices, setInvoices] = useState([]);
//     const [error, setError] = useState(null);
//
//     const fetchInvoices = async () => {
//         try {
//             const response = await fetch('https://ledgerflow-backend-utt2.onrender.com/api/invoices');
//             const data = await response.json();
//             setInvoices(data);
//         } catch (err) {
//             setError("Cannot connect to backend.");
//         }
//     };
//
//     useEffect(() => {
//         fetchInvoices();
//     }, []);
//
//     const handlePayment = async (id, remainingAmount) => {
//         const webhookId = `wh_${Date.now()}`; // Simulates unique webhook to prevent double payment
//         setError(null);
//
//         try {
//             const res = await fetch(`https://ledgerflow-backend-utt2.onrender.com/api/invoices/${id}/pay?amountInCents=${remainingAmount}&webhookId=${webhookId}`, {method: 'POST'});
//             if (!res.ok) throw new Error("Payment failed. Possible concurrency conflict or overpayment.");
//             fetchInvoices();
//         } catch (err) {
//             setError(err.message);
//         }
//     };
//
//     return (
//         <div className="container">
//             <h1>Payment Ledger & Invoices</h1>
//             {error && <div className="error-banner">{error}</div>}
//
//             <div className="invoice-grid">
//                 {invoices.map(inv => (
//                     <div key={inv.id} className="invoice-card">
//                         <h3>Invoice ID: <small>{inv.id.substring(0, 8)}</small></h3>
//                         <p><strong>Status:</strong> <span
//                             className={`badge ${inv.status.toLowerCase()}`}>{inv.status}</span></p>
//                         <hr/>
//                         <p className="section-title">Line Items:</p>
//                         <ul>
//                             {inv.lineItems?.map(item => (
//                                 <li key={item.id}>{item.description} - ${(item.amountInCents / 100).toFixed(2)}</li>
//                             ))}
//                         </ul>
//                         <hr/>
//                         <p><strong>Total:</strong> ${(inv.totalInCents / 100).toFixed(2)}</p>
//                         <p><strong>Paid:</strong> ${(inv.amountPaid / 100).toFixed(2)}</p>
//
//                         {inv.status !== 'PAID' && (
//                             <button onClick={() => handlePayment(inv.id, inv.totalInCents - inv.amountPaid)}>
//                                 Pay Remaining Balance
//                             </button>
//                         )}
//                     </div>
//                 ))}
//             </div>
//         </div>
//     );
// }
//
// export default App;

// =============================================================================================================




import React, { useState, useEffect } from 'react';
import './App.css';

function App() {
    const [invoices, setInvoices] = useState([]);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(true);

    // Fetch real data from your Spring Boot Backend
    const fetchInvoices = async () => {
        setLoading(true);
        try {
            const response = await fetch('https://ledgerflow-backend-utt2.onrender.com/api/invoices');
            if (!response.ok) throw new Error("Backend not reachable");
            const data = await response.json();
            setInvoices(data);
            setError(null);
        } catch (err) {
            setError("Could not connect to the backend. Is Spring Boot running?");
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchInvoices();
    }, []);

    const handlePayment = async (id, remainingAmount) => {
        const webhookId = `wh_${Date.now()}`; // Simulates unique webhook to prevent double payment
        setError(null);

        try {
            const res = await fetch(`https://ledgerflow-backend-utt2.onrender.com/api/invoices/${id}/pay?amountInCents=${remainingAmount}&webhookId=${webhookId}`, { method: 'POST' });
            if (!res.ok) throw new Error("Payment failed. Possible concurrency conflict or overpayment.");
            fetchInvoices();
        } catch (err) {
            alert(err.message);
        }
    };

    // Helper for UI testing if the backend isn't running yet
    const loadMockData = () => {
        setInvoices([
            {
                id: "inv_1a2b3c4d",
                status: "SENT",
                totalInCents: 150000,
                amountPaid: 50000,
                dueDate: new Date().toISOString(),
                lineItems: [
                    { id: "1", description: "Freight Shipping (NY to LA)", amountInCents: 120000 },
                    { id: "2", description: "Fuel Surcharge", amountInCents: 30000 }
                ]
            },
            {
                id: "inv_9z8y7x6w",
                status: "DRAFT",
                totalInCents: 45000,
                amountPaid: 0,
                dueDate: new Date().toISOString(),
                lineItems: [
                    { id: "3", description: "Warehouse Storage (May)", amountInCents: 45000 }
                ]
            }
        ]);
        setError(null);
    };

    const formatCurrency = (cents) => {
        return new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }).format(cents / 100);
    };

    return (
        <div className="app-container">
            <header className="dashboard-header">
                <div>
                  <h1>Accounts Payable</h1>
                    <p className="subtitle">Manage and pay your outstanding invoices</p>
                </div>
              <button id="refreshBtn" className="btn-secondary" onClick={fetchInvoices}>Refresh Data</button>
            </header>

            {error && (
                <div className="error-banner">
                    <p><strong>Connection Error:</strong> {error}</p>
                    <button className="btn-text" onClick={loadMockData}>Load Mock Data for UI Preview</button>
                </div>
            )}

            {loading && !error && <div className="loading-state">Loading invoices...</div>}

            {!loading && invoices.length === 0 && !error && (
                <div className="empty-state">
                    <p>No invoices found in the ledger.</p>
                    <button className="btn-secondary" onClick={loadMockData}>Load Mock Data</button>
                </div>
            )}

            <div className="invoice-grid">
                {invoices.map(inv => {
                    const isPaid = inv.status === 'PAID';
                    const progressPercent = inv.totalInCents > 0 ? (inv.amountPaid / inv.totalInCents) * 100 : 0;
                    const remainingBalance = inv.totalInCents - inv.amountPaid;

                    return (
                        <div key={inv.id} className="invoice-card">
                            <div className="card-header">
                                <div>
                                    <h3 className="invoice-id">#{inv.id.substring(0, 8)}</h3>
                                    <span className="due-date">Due: {new Date(inv.dueDate).toLocaleDateString()}</span>
                                </div>
                                <span className={`status-badge status-${inv.status.toLowerCase()}`}>
                  {inv.status}
                </span>
                            </div>

                            <div className="line-items">
                                <h4>Line Items</h4>
                                <ul>
                                    {inv.lineItems?.length > 0 ? inv.lineItems.map(item => (
                                        <li key={item.id}>
                                            <span>{item.description}</span>
                                            <span className="item-amount">{formatCurrency(item.amountInCents)}</span>
                                        </li>
                                    )) : <li>No line items</li>}
                                </ul>
                            </div>

                            <div className="payment-summary">
                                <div className="summary-row">
                                    <span>Total Amount</span>
                                    <span className="summary-total">{formatCurrency(inv.totalInCents)}</span>
                                </div>

                                <div className="progress-container">
                                    <div className="progress-bar">
                                        <div
                                            className="progress-fill"
                                            style={{ width: `${progressPercent}%`, backgroundColor: isPaid ? '#10b981' : '#3b82f6' }}
                                        ></div>
                                    </div>
                                    <div className="progress-labels">
                                        <span>Paid: {formatCurrency(inv.amountPaid)}</span>
                                        <span>Remaining: {formatCurrency(remainingBalance)}</span>
                                    </div>
                                </div>
                            </div>

                            <div className="card-actions">
                                <button
                                    className={`btn-primary ${isPaid ? 'btn-disabled' : ''}`}
                                    onClick={() => handlePayment(inv.id, remainingBalance)}
                                    disabled={isPaid}
                                >
                                    {isPaid ? 'Fully Paid' : `Pay ${formatCurrency(remainingBalance)}`}
                                </button>
                            </div>
                        </div>
                    );
                })}
            </div>
        </div>
    );
}

export default App;
