import React, { useState, useEffect, useCallback } from "react";
import axios from "axios";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import Navbar from "./components/Navbar";
import BookList from "./components/BookList";
import Cart from "./components/Cart";
import HeaderFooter from "./components/HeaderFooter"; // m Import HeaderFooter component
import "./App.css";

function App() {
	const [books, setBooks] = useState([]);
	const [cartItems, setCartItems] = useState([]);
	const userId = "123"; // Hardcoded for demo

	// Load books from catalog service
	useEffect(() => {
		const abortController = new AbortController();

		axios
			.get("/api/catalog/books", {
				signal: abortController.signal,
			})
			.then((res) => {
				console.log("Books data:", res.data);
				setBooks(res.data);
			})
			.catch((err) => {
				if (!abortController.signal.aborted) {
					console.error("Failed to fetch books:", err);
				}
			});

		return () => abortController.abort();
	}, []);

	// Load cart with cleanup
	const loadCart = useCallback(
		async (abortController) => {
			try {
				const cartRes = await axios.get(`/api/cart/cart/${userId}`, {
					signal: abortController?.signal,
				});
				const cartItems = cartRes.data;

				// Fetch book details for each cart item
				const enrichedCartItems = await Promise.all(
					cartItems.map(async (item) => {
						const bookRes = await axios.get(`/api/catalog/books/${item.bookIsbn}`, {
							signal: abortController?.signal,
						});
						return { ...item, book: bookRes.data };
					})
				);

				setCartItems(enrichedCartItems);
			} catch (err) {
				if (!abortController?.signal.aborted) {
					console.error("Failed to load cart:", err);
				}
			}
		},
		[userId]
	);

	// Initial cart load with cleanup
	useEffect(() => {
		const abortController = new AbortController();
		loadCart(abortController);
		return () => abortController.abort();
	}, [loadCart]);

	// Add item to cart with cleanup
	const addToCart = async (isbn) => {
		const abortController = new AbortController();

		try {
			await axios.post(
				"/api/cart/cart",
				{ userId, bookIsbn: isbn, quantity: 1 },
				{
					headers: { "Content-Type": "application/json" },
					signal: abortController.signal,
				}
			);
			await loadCart(abortController);
			alert("Item added to cart!");
		} catch (err) {
			if (!abortController.signal.aborted) {
				alert(`Error: ${err.response?.data || err.message}`);
			}
		}

		return () => abortController.abort();
	};

	// remove function
	const removeFromCart = async (bookIsbn) => {
		try {
			await axios.delete(`/api/cart/cart/${userId}/${bookIsbn}`);
			await loadCart(); // Refresh cart after removal
			alert("Item removed from cart!");
		} catch (err) {
			alert(`Error: ${err.response?.data || err.message}`);
		}
	};

	return (
		<Router>
			<div className="App">
				<HeaderFooter /> {/* m Includes combined HeaderFooter component */}
				<Navbar cartCount={cartItems.length} />
				<Routes>
					<Route
						path="/"
						element={
							<BookList
								books={books}
								addToCart={addToCart}
							/>
						}
					/>
					<Route
						path="/cart"
						element={
							<Cart
								items={cartItems}
								removeFromCart={removeFromCart}
							/>
						}
					/>
				</Routes>
			</div>
		</Router>
	);
}

export default App;
