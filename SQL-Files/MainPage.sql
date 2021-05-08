-- Currently pretty fast, no need to change

select m.title as 'title', m.year as 'year', m.director as 'director', r.rating as 'rating',
group_concat(distinct g.name) as 'genres',
group_concat(distinct s.name) as 'actors'
From (select r.rating, r.movieId from ratings r order by r.rating desc limit 20) r,movies m, genres_in_movies gim, genres g, stars_in_movies sim, stars s
where r.movieId = m.id AND m.id = gim.movieId and gim.genreId = g.id AND m.id = sim.movieId AND sim.starId = s.id
group by m.title
order by r.rating desc
limit 20
