callback = lambda do |rs, numRow|
    {'id' => rs.getInt('id'), 'name' => rs.getString('name'), 'email' => rs.getString('email')}
end
lista = jdbc.query('SELECT * FROM users', callback)