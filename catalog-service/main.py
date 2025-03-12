from typing import Dict, List, Optional, Union

from database import get_db
from fastapi import Body, Depends, FastAPI, HTTPException, Query
from fastapi.middleware.cors import CORSMiddleware
from models import BookInventory
from pydantic import BaseModel
from sqlalchemy.orm import Session

# from database import get_db, engine  # Import the database session dependency
# import models
# models.Base.metadata.create_all(bind=engine)

app = FastAPI()

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)


# Pydantic Model for Validation
class Book(BaseModel):
    isbn: str
    # id: int
    title: str
    subtitle: Optional[str]
    author: str
    published: str
    publisher: str
    pages: int
    description: str
    price: float
    genre: str
    stock_status: str
    # in_stock: int
    language: str
    rating: Optional[float]
    # in_stock: bool


# # Gets all books
# Optional paramaters title|author|publisher|price range|language|genre|in stock|rating
# Sort by title|author|pages|published|price|rating
@app.get("/books", response_model=List[Book])
def get_books(
    title: Optional[str] = Query(None),
    author: Optional[str] = Query(None),
    publisher: Optional[str] = Query(None),
    published: Optional[str] = Query(None),
    price_min: Optional[float] = Query(None),
    price_max: Optional[float] = Query(None),
    language: Optional[str] = Query(None),
    genre: Optional[str] = Query(None),
    in_stock: Optional[bool] = Query(None),
    min_rating: Optional[float] = Query(None),
    sort_by: Optional[str] = Query(None, pattern="^(title|author|pages|published|price|rating)$"),
    order: Optional[str] = Query("asc", pattern="^(asc|desc)$"),
    db: Session = Depends(get_db),
):
    query = db.query(BookInventory)

    if title:
        query = query.filter(BookInventory.title.ilike(f"%{title.strip()}%"))
    if author:
        query = query.filter(BookInventory.author.ilike(f"%{author.strip()}%"))
    if publisher:
        query = query.filter(BookInventory.publisher.ilike(f"%{publisher.strip()}%"))
    if published:
        query = query.filter(BookInventory.published == published.strip())
    if price_min is not None:
        query = query.filter(BookInventory.price >= price_min)
    if price_max is not None:
        query = query.filter(BookInventory.price <= price_max)
    if language:
        query = query.filter(BookInventory.language.ilike(f"%{language.strip()}%"))
    if genre:
        query = query.filter(BookInventory.genre.ilike(f"%{genre.strip()}%"))
    if in_stock is not None:
        # stock_status = "In Stock" if in_stock else "Out of Stock"
        # query = query.filter(BookInventory.stock_status == stock_status)
        if in_stock:
            query = query.filter(BookInventory.in_stock > 0)
        else:
            query = query.filter(BookInventory.in_stock == 0)
    if min_rating is not None:
        query = query.filter(BookInventory.rating >= min_rating)

    if sort_by:
        reverse = order == "desc"
        query = query.order_by(
            getattr(BookInventory, sort_by).desc() if reverse else getattr(BookInventory, sort_by)
        )

    books = query.all()

    return books


@app.get("/books/{isbn}", response_model=Book)
def get_book(isbn: int, db: Session = Depends(get_db)):
    book = db.query(BookInventory).filter(BookInventory.isbn == str(isbn)).first()
    if book is None:
        raise HTTPException(status_code=404, detail="Book not found")

    return book


# Allowed fields for partial update
ALLOWED_UPDATE_FIELDS = {
    "title",
    "subtitle",
    "author",
    "published",
    "publisher",
    "pages",
    "description",
    "price",
    "genre",
    "language",
    "rating",
}


# Update specific fields of a book record
@app.patch("/books/{isbn}")
def partial_update_book(
    isbn: int,
    book_update: Dict[str, Union[str, int, float]] = Body(
        ...,
        example={
            "title": "Updated Book Title",
            "subtitle": "Updated Book Sub-Title",
            "author": "John Doe",
            "published": "2022",
            "publisher": "Updated Book Publisher",
            "pages": 101,
            "description": "Updated Book Description",
            "price": 29.99,
            "genre": "Fiction",
            "language": "English",
            "rating": 4.5,
            "in_stock": 15,
        },
    ),
    db: Session = Depends(get_db),
):
    book = db.query(BookInventory).filter(BookInventory.isbn == isbn).first()

    if not book:
        raise HTTPException(status_code=404, detail="Book not found")

    # Segregating valid and invalid fields
    invalid_fields = [key for key in book_update if key not in ALLOWED_UPDATE_FIELDS]
    valid_fields = {
        key: value for key, value in book_update.items() if key in ALLOWED_UPDATE_FIELDS
    }

    if not valid_fields:
        raise HTTPException(status_code=400, detail="No valid fields to update")

    # Check if `in_stock` is a valid integer (non-negative) if it's being updated
    if "in_stock" in valid_fields:
        if not isinstance(valid_fields["in_stock"], int):
            raise HTTPException(status_code=400, detail="in_stock must be an integer")
        if valid_fields["in_stock"] < 0:
            raise HTTPException(status_code=400, detail="in_stock must be a non-negative integer")

    # Check `rating` is within the valid range (0-5) if it's being updated
    if "rating" in valid_fields:
        if not isinstance(valid_fields["rating"], (int, float)):
            raise HTTPException(status_code=400, detail="Rating must be a number")
        if not (0.0 <= valid_fields["rating"] <= 5.0):
            raise HTTPException(status_code=400, detail="Rating must be between 0 and 5")

    for key, value in valid_fields.items():
        setattr(book, key, value)

    db.commit()

    response_message = {"updated_book": book}
    if invalid_fields:
        response_message["invalid_fields"] = invalid_fields

    return response_message


# Delete a book by ISBN
@app.delete("/books/{isbn}")
def delete_book(isbn: int, db: Session = Depends(get_db)):
    book = db.query(BookInventory).filter(BookInventory.isbn == isbn).first()

    if not book:
        raise HTTPException(status_code=404, detail="Book not found")

    db.delete(book)
    db.commit()

    return {"message": "Book deleted successfully"}


# Add new book record
@app.post("/books", response_model=Book)
def add_book(book: Book, db: Session = Depends(get_db)):

    existing_book = db.query(BookInventory).filter(BookInventory.title == book.title).first()

    if existing_book:
        raise HTTPException(status_code=400, detail="Book with this title already exists")

    new_book = BookInventory(
        isbn=book.isbn,
        title=book.title,
        subtitle=book.subtitle,
        author=book.author,
        published=book.published,
        publisher=book.publisher,
        pages=book.pages,
        description=book.description,
        price=book.price,
        genre=book.genre,
        language=book.language,
        rating=book.rating,
        in_stock=book.in_stock,
    )

    db.add(new_book)
    db.commit()
    db.refresh(new_book)

    return new_book
