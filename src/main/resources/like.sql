select *
from board
where id in(
    select contentid from likes where userid = 3 and contenttype = 'BOARD')
order by created_at DESC;

select * from comments where id in (select contentid from likes where userid = 2 and contenttype = 'COMMENT');

select contentid from likes where userid = 2 and contenttype = 'COMMENT';
select * from likes where contenttype = 'BOARD' and userid = 3;

select count(*)
from board
where id in(
    select contentid from likes where userid = 3 and contenttype = 'BOARD')
order by created_at DESC;