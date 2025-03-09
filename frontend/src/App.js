import React, { useState, useEffect } from 'react';
import axios from 'axios';
import Navbar from './components/Navbar';
import BookList from './components/BookList';
import Cart from './components/Cart';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import './App.css';

function App() {
    const [books, setBooks] = useState([]);
    const [cartItems, setCartItems] = useState([]);
    const userId = "123"; // Hardcoded for demo

    // Load books from catalog service
    useEffect(() => {
        axios.get('http://localhost:8000/books')
            .then((res) => {
                console.log("Books data:", res.data); // Debug: Check if data is received
                setBooks(res.data);
            })
            .catch((err) => {
                console.error("Failed to fetch books:", err); // Debug: Check for errors
            });
    }, []);

    const loadCart = () => {
        axios.get(`http://localhost:8080/cart/${userId}`)
            .then((cartRes) => {
                const cartItems = cartRes.data;

                // Fetch book details for each item in the cart
                const bookRequests = cartItems.map(item =>
                    axios.get(`http://localhost:8000/books/${item.bookIsbn}`)
                );

                Promise.all(bookRequests)
                    .then((bookResponses) => {
                        const enrichedCartItems = cartItems.map((item, index) => ({
                            ...item,
                            book: bookResponses[index].data // Add book details
                        }));
                        setCartItems(enrichedCartItems);
                    })
                    .catch(console.error);
            })
            .catch(console.error);
    };

    // Initial cart load
    useEffect(loadCart, []);

    // Add item to cart
    const addToCart = (isbn) => {
        axios.post(
            "http://localhost:8080/cart",
            { userId, bookIsbn: isbn, quantity: 1 }, // Send as JSON body
            { headers: { "Content-Type": "application/json" } } // Explicitly set headers
        )
            .then(() => {
                loadCart();
                alert('Item added to cart!');
            })
            .catch((err) => alert(`Error: ${err.response?.data || err.message}`));
    };

    return (
        <Router>
            <div className="App">
                <Navbar cartCount={cartItems.length} />
                <Routes>
                    <Route path="/" element={<BookList books={books} addToCart={addToCart} />} />
                    <Route path="/cart" element={<Cart items={cartItems} />} />
                </Routes>
            </div>
        </Router>
    );
}

export default App;