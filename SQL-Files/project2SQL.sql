-- singleStar SQL

-- select s.name as 'starName', s.birthYear as 'birthYear',
-- group_concat(m.title order by m.year DESC separator '|') as 'movies',
-- group_concat(m.id order by m.year DESC separator '|') as 'movieID'
-- from stars s
-- left join stars_in_movies sim on sim.starId = s.id
-- left join movies m on m.id = sim.movieId
-- where s.id = "nm9423474"
-- order by m.year desc