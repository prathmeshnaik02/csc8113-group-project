import React from 'react';
import axios from 'axios';

// Add item to cart
const addToCart = (isbn) => {
    // Hardcoded userId for testing (replace with real auth later)
    const userId = "123";

    axios.post(`http://localhost:8080/cart?userId=${userId}&bookIsbn=${isbn}&quantity=1`)
        .then(() => alert('Item added to cart!'))
        .catch((err) => alert('Failed to add item: ' + err.message));
};

const BookList = ({ books }) => {
    return (
        <div className="book-list">
            {books.map((book) => (
                <div key={book.isbn} className="book">
                    <h3>{book.title}</h3>
                    <p>ISBN: {book.isbn}</p>
                    <p>Author: {book.author}</p>
                    <p>Price: ${book.price}</p>
                    <button onClick={() => addToCart(book.isbn)}>Add to Cart</button>
                </div>
            ))}
        </div>
    );
};

export default BookList;