<h2>Описание предметной области:</h2>
<p>Book: имеет id, название, описание, ISBN, авторов, издателя.</p>
<p> Author: имеет id, имя. Связь с книгой @ManyToMany. </p>
<p> Publisher: : имеет id,  имя, адрес. Связь с книгой @OneToMany. </p>
<h2>Примеры endpoint:</h2>
- Книга 
![ime](https://github.com/OlyaEna/jdbc_servlet_app/blob/master/src/main/resources/img/all_books.JPG)
![ime](https://github.com/OlyaEna/jdbc_servlet_app/blob/master/src/main/resources/img/bookbyid.JPG)
![ime](https://github.com/OlyaEna/jdbc_servlet_app/blob/master/src/main/resources/img/bookbyid.JPG)
![ime](https://github.com/OlyaEna/jdbc_servlet_app/blob/master/src/main/resources/img/create_book.JPG)
-Автор
![ime](https://github.com/OlyaEna/jdbc_servlet_app/blob/master/src/main/resources/img/all_authors.JPG)
![ime](https://github.com/OlyaEna/jdbc_servlet_app/blob/master/src/main/resources/img/author_delete.JPG)
![ime](https://github.com/OlyaEna/jdbc_servlet_app/blob/master/src/main/resources/img/author_update.JPG)
![ime](https://github.com/OlyaEna/jdbc_servlet_app/blob/master/src/main/resources/img/authorbyid.JPG)
![ime](https://github.com/OlyaEna/jdbc_servlet_app/blob/master/src/main/resources/img/authorbyname.JPG)
![ime](https://github.com/OlyaEna/jdbc_servlet_app/blob/master/src/main/resources/img/create_author.JPG)

