<h2>Задача «Агент в стохастической среде»</h2> Агент находится в среде M*N. Начиная с
начального состояния, он должен выбирать действие на каждом временном шаге.
Взаимодействие со средой прекращается, когда агент достигает одного из целевых
состояний, отмеченных +1 или —1.\
Действия в каждом состоянии: {Вверх, Вниз, Влево и Вправо}. На
данный момент мы предполагаем, что окружающая среда полностью наблюдаема, так что агент
всегда знает, где она находится. Если бы среда была детерминированной, решение было бы
простым: [Вверх, Вверх, Вправо, Вправо, Вправо]. К сожалению, окружающая среда не всегда
соглашается с этим решением, поскольку действия ненадежны.