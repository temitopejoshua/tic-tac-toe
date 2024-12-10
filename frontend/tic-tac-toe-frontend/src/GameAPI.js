const BASE_URL = 'http://192.168.216.125:5050'

export const resetGame = async (difficultyLevel) =>  await fetch(BASE_URL + '/api/v1/game/tic-tac-toe/reset?difficultyLevel=' + difficultyLevel, {
    method: 'PUT'
  });

  export const playGame = async(moveNumber, difficulty) => await fetch(BASE_URL + "/api/v1/game/tic-tac-toe/play", {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
        "move": moveNumber,
        "difficulty": difficulty
    })
  });