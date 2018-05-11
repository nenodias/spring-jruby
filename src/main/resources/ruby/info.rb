callback = lambda do |rs, numRow|
    {'id' => rs.getInt('id'), 'nome' => rs.getString('nome'), 'idade' => rs.getInt('idade')}
end
lista = jdbc.query('SELECT * FROM USERS', callback)