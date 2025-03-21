import React from 'react';

const Cart = ({ items, removeFromCart }) => {
    return (
        <div className="cart">
            <h2>Your Cart</h2>
            {items.length === 0 ? (
                <p>Your cart is empty</p>
            ) : (
                <div className="cart-items">
                    {items.map((item) => (
                        <div key={item.bookIsbn} className="cart-item">
                            <h3>{item.book.title}</h3>
                            <p>Quantity: {item.quantity}</p>
                            <p>Price: ${item.book.price}</p>
                            <button onClick={() => removeFromCart(item.bookIsbn)} className="remove-button">
                                Remove
                            </button>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};

export default Cart;