-- sql script for single movie page
select m.title as 'title', m.year as 'year', m.director as 'director', group_concat(distinct g.name) as genres ,tester.*, r.rating as 'rating'
from stars_in_movies sim, stars s,
(select group_concat(test.name) as "actors", group_concat(test.starzId) as "starId" from
(select s.id as 'starzId', s.name as 'name', count(s.name) as moviesStarredIn
from stars_in_movies sim, stars s, movies m,
(select sim.starId from stars_in_movies sim, stars s where sim.starId = s.id and sim.movieId = ?) as starIdentifier
where s.id = starIdentifier.starId and sim.starId = s.id and m.id = sim.movieId
group by s.name
order by moviesStarredIn desc) as test) as tester,
genres_in_movies gim, genres g , movies m
left join ratings r on r.movieId = m.id
where m.id = ? and m.id = sim.movieId and sim.starId = s.id and m.id = gim.movieId and g.id = gim.genreId
group by title

-- sql script for single actor page
select s.name as 'starName', s.birthYear as 'birthYear',
group_concat(m.title order by m.year DESC separator '|') as 'movies',
group_concat(m.id order by m.year DESC separator '|') as 'movieID'
from movies m, stars s, stars_in_movies sim
where sim.starId = s.id and s.id = 'nm0351006' and m.id = sim.movieId
order by m.year desc