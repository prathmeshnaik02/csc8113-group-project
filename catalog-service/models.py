from database import Base
from sqlalchemy import ARRAY, Boolean, Column, Float, ForeignKey, Integer, String


class BookInventory(Base):
    # __tablename__ = "bookInventory"
    __tablename__ = "book"

    # isbn = Column(String, primary_key=True, index=True)
    id = Column(Integer, primary_key=True, index=True)
    title = Column(String, nullable=False, index=True)
    # subtitle = Column(String, nullable=True)
    # author = Column(String, nullable=False, index=True)
    # published = Column(String, nullable=False, index=True)
    # publisher = Column(String, nullable=False, index=True)
    # pages = Column(Integer, nullable=False)
    # description = Column(String, nullable=False)
    price = Column(Float, nullable=False, index=True)
    # genre = Column(String, nullable=False, index=True)
    # stock_status = Column(String, nullable=False, index=True)
    stock = Column(Integer, nullable=False, index=True)
    # language = Column(String, nullable=False, index=True)
    # rating = Column(Float, nullable=True, index=True)
