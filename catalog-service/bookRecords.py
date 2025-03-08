from sqlalchemy.orm import Session
from database import get_db
import models

books = [
    {
        "id": 1,
        "title": "The Great Adventure",
        "subtitle": "A Journey Through Time",
        "author": "John Doe",
        "published": "2021",
        "publisher": "Adventure Press",
        "pages": 350,
        "description": "An epic story of adventure through historical events.",
        "price": 19.99,
        "genre": "Adventure",
        "in_stock": 20,
        "language": "English",
        "rating": 4.5,
    },
    {
        "id": 2,
        "title": "Tech Innovations",
        "subtitle": "The Future of Technology",
        "author": "Alice Johnson",
        "published": "2022",
        "publisher": "TechWorld Publications",
        "pages": 250,
        "description": "An exploration of groundbreaking technologies changing the world.",
        "price": 24.99,
        "genre": "Technology",
        "in_stock": 0,
        "language": "English",
        "rating": 4.2,
    },
    {
        "id": 3,
        "title": "Mystery of the Missing Diamond",
        "subtitle": None,
        "author": "Michael Lee",
        "published": "2019",
        "publisher": "Mystery Books",
        "pages": 555,
        "description": "A thrilling mystery novel about a stolen diamond and the detective who seeks to recover it.",
        "price": 14.99,
        "genre": "Mystery",
        "in_stock": 20,
        "language": "English",
        "rating": 4.8,
    },
    {
        "id": 4,
        "title": "The Lost World",
        "subtitle": "Discoveries Beneath the Surface",
        "author": "Sarah Johnson",
        "published": "2020",
        "publisher": "Mystery Press",
        "pages": 285,
        "description": "A thrilling tale of uncharted territories and the mysteries they hide.",
        "price": 15.99,
        "genre": "Adventure",
        "in_stock": 20,
        "language": "English",
        "rating": 4.3,
    },
    {
        "id": 5,
        "title": "Beyond the Horizon",
        "subtitle": "Exploring New Frontiers",
        "author": "Michael White",
        "published": "2018",
        "publisher": "Explorer Books",
        "pages": 400,
        "description": "A gripping narrative of human exploration and discovery across the globe.",
        "price": 22.50,
        "genre": "Non-fiction",
        "in_stock": 0,
        "language": "English",
        "rating": 4.7,
    },
]


def insert_or_update_books(db: Session):
    for book in books:

        existing_book = (
            db.query(models.BookInventory)
            .filter(models.BookInventory.id == book["id"])
            .first()
        )

        if existing_book:

            existing_book.title = book["title"]
            existing_book.subtitle = book["subtitle"]
            existing_book.author = book["author"]
            existing_book.published = book["published"]
            existing_book.publisher = book["publisher"]
            existing_book.pages = book["pages"]
            existing_book.description = book["description"]
            existing_book.price = book["price"]
            existing_book.genre = book["genre"]
            existing_book.in_stock = book["in_stock"]
            existing_book.language = book["language"]
            existing_book.rating = book["rating"]
        else:

            db_book = models.BookInventory(
                id=book["id"],
                title=book["title"],
                subtitle=book["subtitle"],
                author=book["author"],
                published=book["published"],
                publisher=book["publisher"],
                pages=book["pages"],
                description=book["description"],
                price=book["price"],
                genre=book["genre"],
                in_stock=book["in_stock"],
                language=book["language"],
                rating=book["rating"],
            )

            db.add(db_book)

    db.commit()


def main():
    db = next(get_db())
    insert_or_update_books(db)

    print("Books have been inserted or updated successfully.")


if __name__ == "__main__":
    main()
