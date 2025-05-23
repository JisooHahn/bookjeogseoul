select  book_isbn, count(*) as count
from tbl_book_post_like bpl
         join tbl_book_post bp
              on bpl.book_post_id = bp.id
group by book_isbn
order by count desc
limit 10;

