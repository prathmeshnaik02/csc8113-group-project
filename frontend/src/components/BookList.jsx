import React from "react";
import axios from "axios";

// Add item to cart
const addToCart = (isbn) => {
	const userId = "123"; // Hardcoded for testing

	axios
		.post(
			"/api/cart/cart", // URL without query parameters
			{ userId, bookIsbn: isbn, quantity: 1 }, // JSON body
			{ headers: { "Content-Type": "application/json" } } // Required headers
		)
		.then(() => alert("Item added to cart!"))
		.catch((err) => alert("Failed to add item: " + err.message));
};

const BookList = ({ books }) => {
	return (
		<div className="book-list">
			{books.map((book) => (
				<div
					key={book.isbn}
					className="book"
				>
					<h3>{book.title}</h3>
					<p>
						<b>ISBN:</b> {book.isbn}
					</p>
					<p>
						<b>Author:</b> {book.author}
					</p>
					<p>
						<b>Price:</b> ${book.price}
					</p>
					<button onClick={() => addToCart(book.isbn)}>Add to Cart</button>
				</div>
			))}
		</div>
	);
};

export default BookList;
