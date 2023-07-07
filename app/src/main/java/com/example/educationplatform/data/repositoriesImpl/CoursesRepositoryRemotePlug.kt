package com.example.educationplatform.data.repositoriesImpl

import com.example.educationplatform.domain.entities.*
import com.example.educationplatform.domain.repositories.CoursesRepositoryRemote
import kotlinx.coroutines.delay
import javax.inject.Inject

class CoursesRepositoryRemotePlug @Inject constructor(): CoursesRepositoryRemote {
    override suspend fun getTakesCourses(): Result<List<SubCourse>> {
        delay(2000)
        return Result.success(listOf(
            SubCourse(1, "Python основы", "https://office-guru.ru/wp-content/uploads/2022/01/3-335x220.jpeg", 100, 10),
            SubCourse(2, "Java основы", "https://i.ibb.co/r7svSCs/python.jpg", 100, 10),
            SubCourse(3, "Kotlin основы", "https://office-guru.ru/wp-content/uploads/2022/01/3-335x220.jpeg", 100, 10),
            SubCourse(4, "C# основы", "https://i.ibb.co/r7svSCs/python.jpg", 100, 10),
            SubCourse(5, "C++ основы", "https://office-guru.ru/wp-content/uploads/2022/01/3-335x220.jpeg", 100, 10),
            SubCourse(6, "SQL основы", "https://i.ibb.co/r7svSCs/python.jpg", 100, 10),
            SubCourse(7, "HTML основы", "https://i.ibb.co/r7svSCs/python.jpg", 100, 10),
            SubCourse(8, "Visual paradigm", "https://office-guru.ru/wp-content/uploads/2022/01/3-335x220.jpeg", 100, 10)
        ))
    }

    override suspend fun getEditCourses(): Result<List<UsersCourse>> {
        delay(2000)
        return Result.success(listOf(
            UsersCourse(1, "Python основы", "https://i.ibb.co/r7svSCs/python.jpg"),
            UsersCourse(2, "Python основы", "https://i.ibb.co/r7svSCs/python.jpg"),
            UsersCourse(3, "Python основы", "https://i.ibb.co/r7svSCs/python.jpg"),
            UsersCourse(4, "", "https://i.ibb.co/r7svSCs/python.jpg"),
            UsersCourse(5, "", "https://i.ibb.co/r7svSCs/python.jpg"),
            UsersCourse(6, "", "https://i.ibb.co/r7svSCs/python.jpg"),
            UsersCourse(7, "", "https://i.ibb.co/r7svSCs/python.jpg"),
            UsersCourse(8, "", "https://i.ibb.co/r7svSCs/python.jpg"),
        ))
    }

    override suspend fun getCourses(): Result<List<Course>> {
        delay(2000)
        return Result.success(listOf(
            Course(1, "Python основы", "Высокоуровневый язык программирования общего назначения с динамической строгой типизацией и автоматическим управлением памятью, ориентированный на повышение производительности разработчика, читаемости кода и его качества, а также на обеспечение переносимости написанных на нём программ. Язык является полностью объектно-ориентированным в том плане, что всё является объектами. Необычной особенностью языка является выделение блоков кода пробельными отступами. Синтаксис ядра языка минималистичен, за счёт чего на практике редко возникает необходимость обращаться к документации. Сам же язык известен как интерпретируемый и используется в том числе для написания скриптов...","Python",1,4,10,"https://office-guru.ru/wp-content/uploads/2022/01/3-335x220.jpeg"),
            Course(1, "Python основы", "Высокоуровневый язык программирования общего назначения с динамической строгой типизацией и автоматическим управлением памятью, ориентированный на повышение производительности разработчика, читаемости кода и его качества, а также на обеспечение переносимости написанных на нём программ. Язык является полностью объектно-ориентированным в том плане, что всё является объектами. Необычной особенностью языка является выделение блоков кода пробельными отступами. Синтаксис ядра языка минималистичен, за счёт чего на практике редко возникает необходимость обращаться к документации. Сам же язык известен как интерпретируемый и используется в том числе для написания скриптов...","Python",1,4,10,"https://office-guru.ru/wp-content/uploads/2022/01/3-335x220.jpeg"),
            Course(1, "Python основы", "Высокоуровневый язык программирования общего назначения с динамической строгой типизацией и автоматическим управлением памятью, ориентированный на повышение производительности разработчика, читаемости кода и его качества, а также на обеспечение переносимости написанных на нём программ. Язык является полностью объектно-ориентированным в том плане, что всё является объектами. Необычной особенностью языка является выделение блоков кода пробельными отступами. Синтаксис ядра языка минималистичен, за счёт чего на практике редко возникает необходимость обращаться к документации. Сам же язык известен как интерпретируемый и используется в том числе для написания скриптов...","Python",1,4,10,"https://office-guru.ru/wp-content/uploads/2022/01/3-335x220.jpeg"),
            Course(1, "Python основы", "Высокоуровневый язык программирования общего назначения с динамической строгой типизацией и автоматическим управлением памятью, ориентированный на повышение производительности разработчика, читаемости кода и его качества, а также на обеспечение переносимости написанных на нём программ. Язык является полностью объектно-ориентированным в том плане, что всё является объектами. Необычной особенностью языка является выделение блоков кода пробельными отступами. Синтаксис ядра языка минималистичен, за счёт чего на практике редко возникает необходимость обращаться к документации. Сам же язык известен как интерпретируемый и используется в том числе для написания скриптов...","Python",1,4,10,"https://office-guru.ru/wp-content/uploads/2022/01/3-335x220.jpeg"),
            Course(1, "Python основы", "Высокоуровневый язык программирования общего назначения с динамической строгой типизацией и автоматическим управлением памятью, ориентированный на повышение производительности разработчика, читаемости кода и его качества, а также на обеспечение переносимости написанных на нём программ. Язык является полностью объектно-ориентированным в том плане, что всё является объектами. Необычной особенностью языка является выделение блоков кода пробельными отступами. Синтаксис ядра языка минималистичен, за счёт чего на практике редко возникает необходимость обращаться к документации. Сам же язык известен как интерпретируемый и используется в том числе для написания скриптов...","Python",1,4,10,"https://office-guru.ru/wp-content/uploads/2022/01/3-335x220.jpeg"),
            Course(1, "Python основы", "Высокоуровневый язык программирования общего назначения с динамической строгой типизацией и автоматическим управлением памятью, ориентированный на повышение производительности разработчика, читаемости кода и его качества, а также на обеспечение переносимости написанных на нём программ. Язык является полностью объектно-ориентированным в том плане, что всё является объектами. Необычной особенностью языка является выделение блоков кода пробельными отступами. Синтаксис ядра языка минималистичен, за счёт чего на практике редко возникает необходимость обращаться к документации. Сам же язык известен как интерпретируемый и используется в том числе для написания скриптов...","Python",1,4,10,"https://office-guru.ru/wp-content/uploads/2022/01/3-335x220.jpeg"),
            Course(1, "Python основы", "Высокоуровневый язык программирования общего назначения с динамической строгой типизацией и автоматическим управлением памятью, ориентированный на повышение производительности разработчика, читаемости кода и его качества, а также на обеспечение переносимости написанных на нём программ. Язык является полностью объектно-ориентированным в том плане, что всё является объектами. Необычной особенностью языка является выделение блоков кода пробельными отступами. Синтаксис ядра языка минималистичен, за счёт чего на практике редко возникает необходимость обращаться к документации. Сам же язык известен как интерпретируемый и используется в том числе для написания скриптов...","Python",1,4,10,"https://office-guru.ru/wp-content/uploads/2022/01/3-335x220.jpeg"),
            Course(1, "Python основы", "Высокоуровневый язык программирования общего назначения с динамической строгой типизацией и автоматическим управлением памятью, ориентированный на повышение производительности разработчика, читаемости кода и его качества, а также на обеспечение переносимости написанных на нём программ. Язык является полностью объектно-ориентированным в том плане, что всё является объектами. Необычной особенностью языка является выделение блоков кода пробельными отступами. Синтаксис ядра языка минималистичен, за счёт чего на практике редко возникает необходимость обращаться к документации. Сам же язык известен как интерпретируемый и используется в том числе для написания скриптов...","Python",1,4,10,"https://office-guru.ru/wp-content/uploads/2022/01/3-335x220.jpeg"),
            Course(1, "Python основы", "Высокоуровневый язык программирования общего назначения с динамической строгой типизацией и автоматическим управлением памятью, ориентированный на повышение производительности разработчика, читаемости кода и его качества, а также на обеспечение переносимости написанных на нём программ. Язык является полностью объектно-ориентированным в том плане, что всё является объектами. Необычной особенностью языка является выделение блоков кода пробельными отступами. Синтаксис ядра языка минималистичен, за счёт чего на практике редко возникает необходимость обращаться к документации. Сам же язык известен как интерпретируемый и используется в том числе для написания скриптов...","Python",1,4,10,"https://office-guru.ru/wp-content/uploads/2022/01/3-335x220.jpeg"),
            Course(1, "Python основы", "Высокоуровневый язык программирования общего назначения с динамической строгой типизацией и автоматическим управлением памятью, ориентированный на повышение производительности разработчика, читаемости кода и его качества, а также на обеспечение переносимости написанных на нём программ. Язык является полностью объектно-ориентированным в том плане, что всё является объектами. Необычной особенностью языка является выделение блоков кода пробельными отступами. Синтаксис ядра языка минималистичен, за счёт чего на практике редко возникает необходимость обращаться к документации. Сам же язык известен как интерпретируемый и используется в том числе для написания скриптов...","Python",1,4,10,"https://office-guru.ru/wp-content/uploads/2022/01/3-335x220.jpeg"),
            Course(1, "Python основы", "Высокоуровневый язык программирования общего назначения с динамической строгой типизацией и автоматическим управлением памятью, ориентированный на повышение производительности разработчика, читаемости кода и его качества, а также на обеспечение переносимости написанных на нём программ. Язык является полностью объектно-ориентированным в том плане, что всё является объектами. Необычной особенностью языка является выделение блоков кода пробельными отступами. Синтаксис ядра языка минималистичен, за счёт чего на практике редко возникает необходимость обращаться к документации. Сам же язык известен как интерпретируемый и используется в том числе для написания скриптов...","Python",1,4,10,"https://office-guru.ru/wp-content/uploads/2022/01/3-335x220.jpeg"),
            Course(1, "Python основы", "Высокоуровневый язык программирования общего назначения с динамической строгой типизацией и автоматическим управлением памятью, ориентированный на повышение производительности разработчика, читаемости кода и его качества, а также на обеспечение переносимости написанных на нём программ. Язык является полностью объектно-ориентированным в том плане, что всё является объектами. Необычной особенностью языка является выделение блоков кода пробельными отступами. Синтаксис ядра языка минималистичен, за счёт чего на практике редко возникает необходимость обращаться к документации. Сам же язык известен как интерпретируемый и используется в том числе для написания скриптов...","Python",1,4,10,"https://office-guru.ru/wp-content/uploads/2022/01/3-335x220.jpeg"),
            Course(1, "Python основы", "Высокоуровневый язык программирования общего назначения с динамической строгой типизацией и автоматическим управлением памятью, ориентированный на повышение производительности разработчика, читаемости кода и его качества, а также на обеспечение переносимости написанных на нём программ. Язык является полностью объектно-ориентированным в том плане, что всё является объектами. Необычной особенностью языка является выделение блоков кода пробельными отступами. Синтаксис ядра языка минималистичен, за счёт чего на практике редко возникает необходимость обращаться к документации. Сам же язык известен как интерпретируемый и используется в том числе для написания скриптов...","Python",1,4,10,"https://office-guru.ru/wp-content/uploads/2022/01/3-335x220.jpeg"),
            Course(1, "Python основы", "Высокоуровневый язык программирования общего назначения с динамической строгой типизацией и автоматическим управлением памятью, ориентированный на повышение производительности разработчика, читаемости кода и его качества, а также на обеспечение переносимости написанных на нём программ. Язык является полностью объектно-ориентированным в том плане, что всё является объектами. Необычной особенностью языка является выделение блоков кода пробельными отступами. Синтаксис ядра языка минималистичен, за счёт чего на практике редко возникает необходимость обращаться к документации. Сам же язык известен как интерпретируемый и используется в том числе для написания скриптов...","Python",1,4,10,"https://office-guru.ru/wp-content/uploads/2022/01/3-335x220.jpeg"),
            Course(1, "Python основы", "Высокоуровневый язык программирования общего назначения с динамической строгой типизацией и автоматическим управлением памятью, ориентированный на повышение производительности разработчика, читаемости кода и его качества, а также на обеспечение переносимости написанных на нём программ. Язык является полностью объектно-ориентированным в том плане, что всё является объектами. Необычной особенностью языка является выделение блоков кода пробельными отступами. Синтаксис ядра языка минималистичен, за счёт чего на практике редко возникает необходимость обращаться к документации. Сам же язык известен как интерпретируемый и используется в том числе для написания скриптов...","Python",1,4,10,"https://office-guru.ru/wp-content/uploads/2022/01/3-335x220.jpeg"),
            Course(1, "Python основы", "Высокоуровневый язык программирования общего назначения с динамической строгой типизацией и автоматическим управлением памятью, ориентированный на повышение производительности разработчика, читаемости кода и его качества, а также на обеспечение переносимости написанных на нём программ. Язык является полностью объектно-ориентированным в том плане, что всё является объектами. Необычной особенностью языка является выделение блоков кода пробельными отступами. Синтаксис ядра языка минималистичен, за счёт чего на практике редко возникает необходимость обращаться к документации. Сам же язык известен как интерпретируемый и используется в том числе для написания скриптов...","Python",1,4,10,"https://office-guru.ru/wp-content/uploads/2022/01/3-335x220.jpeg"),
            Course(1, "Python основы", "Высокоуровневый язык программирования общего назначения с динамической строгой типизацией и автоматическим управлением памятью, ориентированный на повышение производительности разработчика, читаемости кода и его качества, а также на обеспечение переносимости написанных на нём программ. Язык является полностью объектно-ориентированным в том плане, что всё является объектами. Необычной особенностью языка является выделение блоков кода пробельными отступами. Синтаксис ядра языка минималистичен, за счёт чего на практике редко возникает необходимость обращаться к документации. Сам же язык известен как интерпретируемый и используется в том числе для написания скриптов...","Python",1,4,10,"https://office-guru.ru/wp-content/uploads/2022/01/3-335x220.jpeg"),
            Course(1, "Python основы", "Высокоуровневый язык программирования общего назначения с динамической строгой типизацией и автоматическим управлением памятью, ориентированный на повышение производительности разработчика, читаемости кода и его качества, а также на обеспечение переносимости написанных на нём программ. Язык является полностью объектно-ориентированным в том плане, что всё является объектами. Необычной особенностью языка является выделение блоков кода пробельными отступами. Синтаксис ядра языка минималистичен, за счёт чего на практике редко возникает необходимость обращаться к документации. Сам же язык известен как интерпретируемый и используется в том числе для написания скриптов...","Python",1,4,10,"https://office-guru.ru/wp-content/uploads/2022/01/3-335x220.jpeg"),
            Course(1, "Python основы", "Высокоуровневый язык программирования общего назначения с динамической строгой типизацией и автоматическим управлением памятью, ориентированный на повышение производительности разработчика, читаемости кода и его качества, а также на обеспечение переносимости написанных на нём программ. Язык является полностью объектно-ориентированным в том плане, что всё является объектами. Необычной особенностью языка является выделение блоков кода пробельными отступами. Синтаксис ядра языка минималистичен, за счёт чего на практике редко возникает необходимость обращаться к документации. Сам же язык известен как интерпретируемый и используется в том числе для написания скриптов...","Python",1,4,10,"https://office-guru.ru/wp-content/uploads/2022/01/3-335x220.jpeg"),
            Course(1, "Python основы", "Высокоуровневый язык программирования общего назначения с динамической строгой типизацией и автоматическим управлением памятью, ориентированный на повышение производительности разработчика, читаемости кода и его качества, а также на обеспечение переносимости написанных на нём программ. Язык является полностью объектно-ориентированным в том плане, что всё является объектами. Необычной особенностью языка является выделение блоков кода пробельными отступами. Синтаксис ядра языка минималистичен, за счёт чего на практике редко возникает необходимость обращаться к документации. Сам же язык известен как интерпретируемый и используется в том числе для написания скриптов...","Python",1,4,10,"https://office-guru.ru/wp-content/uploads/2022/01/3-335x220.jpeg"),
            Course(1, "Python основы", "Высокоуровневый язык программирования общего назначения с динамической строгой типизацией и автоматическим управлением памятью, ориентированный на повышение производительности разработчика, читаемости кода и его качества, а также на обеспечение переносимости написанных на нём программ. Язык является полностью объектно-ориентированным в том плане, что всё является объектами. Необычной особенностью языка является выделение блоков кода пробельными отступами. Синтаксис ядра языка минималистичен, за счёт чего на практике редко возникает необходимость обращаться к документации. Сам же язык известен как интерпретируемый и используется в том числе для написания скриптов...","Python",1,4,10,"https://office-guru.ru/wp-content/uploads/2022/01/3-335x220.jpeg"),
            Course(1, "Python основы", "Высокоуровневый язык программирования общего назначения с динамической строгой типизацией и автоматическим управлением памятью, ориентированный на повышение производительности разработчика, читаемости кода и его качества, а также на обеспечение переносимости написанных на нём программ. Язык является полностью объектно-ориентированным в том плане, что всё является объектами. Необычной особенностью языка является выделение блоков кода пробельными отступами. Синтаксис ядра языка минималистичен, за счёт чего на практике редко возникает необходимость обращаться к документации. Сам же язык известен как интерпретируемый и используется в том числе для написания скриптов...","Python",1,4,10,"https://office-guru.ru/wp-content/uploads/2022/01/3-335x220.jpeg"),
            Course(1, "Python основы", "Высокоуровневый язык программирования общего назначения с динамической строгой типизацией и автоматическим управлением памятью, ориентированный на повышение производительности разработчика, читаемости кода и его качества, а также на обеспечение переносимости написанных на нём программ. Язык является полностью объектно-ориентированным в том плане, что всё является объектами. Необычной особенностью языка является выделение блоков кода пробельными отступами. Синтаксис ядра языка минималистичен, за счёт чего на практике редко возникает необходимость обращаться к документации. Сам же язык известен как интерпретируемый и используется в том числе для написания скриптов...","Python",1,4,10,"https://office-guru.ru/wp-content/uploads/2022/01/3-335x220.jpeg"),
            Course(1, "Python основы", "Высокоуровневый язык программирования общего назначения с динамической строгой типизацией и автоматическим управлением памятью, ориентированный на повышение производительности разработчика, читаемости кода и его качества, а также на обеспечение переносимости написанных на нём программ. Язык является полностью объектно-ориентированным в том плане, что всё является объектами. Необычной особенностью языка является выделение блоков кода пробельными отступами. Синтаксис ядра языка минималистичен, за счёт чего на практике редко возникает необходимость обращаться к документации. Сам же язык известен как интерпретируемый и используется в том числе для написания скриптов...","Python",1,4,10,"https://office-guru.ru/wp-content/uploads/2022/01/3-335x220.jpeg"),
            Course(1, "Python основы", "Высокоуровневый язык программирования общего назначения с динамической строгой типизацией и автоматическим управлением памятью, ориентированный на повышение производительности разработчика, читаемости кода и его качества, а также на обеспечение переносимости написанных на нём программ. Язык является полностью объектно-ориентированным в том плане, что всё является объектами. Необычной особенностью языка является выделение блоков кода пробельными отступами. Синтаксис ядра языка минималистичен, за счёт чего на практике редко возникает необходимость обращаться к документации. Сам же язык известен как интерпретируемый и используется в том числе для написания скриптов...","Python",1,4,10,"https://office-guru.ru/wp-content/uploads/2022/01/3-335x220.jpeg"),

        ))
    }

    override suspend fun isUserInCourse(courseId: Int): Result<Boolean> {
        delay(2000)
        return Result.success(true)
    }

    override suspend fun getFullCourseInfo(courseId: Int): Result<Course> {
        delay(2000)
        return Result.success(Course(1, "Python основы", "Высокоуровневый язык программирования общего назначения с динамической строгой типизацией и автоматическим управлением памятью, ориентированный на повышение производительности разработчика, читаемости кода и его качества, а также на обеспечение переносимости написанных на нём программ. Язык является полностью объектно-ориентированным в том плане, что всё является объектами. Необычной особенностью языка является выделение блоков кода пробельными отступами. Синтаксис ядра языка минималистичен, за счёт чего на практике редко возникает необходимость обращаться к документации. Сам же язык известен как интерпретируемый и используется в том числе для написания скриптов...","Python",1,4,10,"https://office-guru.ru/wp-content/uploads/2022/01/3-335x220.jpeg"))
    }

    override suspend fun getDirections(): Result<List<String>> {
        delay(2000)
        return Result.success(listOf("Python", "C#", "Java", "Algorithms", "Android", "HTML", "Internet programming"))
    }

    override suspend fun createCourse(course: Course): Result<Int> {
        delay(2000)
        return Result.success(10)
    }

    override suspend fun updateCourse(course: Course): Result<Unit> {
        delay(2000)
        return Result.success(Unit)
    }

    override suspend fun deleteCourse(courseId: Int): Result<Unit> {
        delay(2000)
        return Result.success(Unit)
    }

    override suspend fun createModule(module: Module): Result<Int> {
        delay(2000)
        return Result.success(1)
    }

    override suspend fun createStage(stage: Stage): Result<Int> {
        delay(2000)
        return Result.success(1)
    }

    override suspend fun getModulesByCourseId(courseId: Int): Result<List<Module>> {
        delay(2000)
        return Result.success(
            listOf(
                Module(1, "Start education", "Learning the basics", 1, 100,
                    mutableListOf(
                        Stage(1, "Coding", "Coding", "<h1 style=\"text-align: center;\">Wellcome!!!</h1><div><ul><li>Base python</li><li>Base syntaxis</li><li>Base math operations</li></ul><div><b>Very important teach the base information about python, because writing code on python and anyone</b><b style=\"\"><i>&nbsp;language is</i>sss</b></div></div>", "Lecture", 1, 10),
                        Stage(2, "Coding", "Coding", "{\"testData\":[],\"diagramData\":\"{\\\"blocks\\\":[{\\\"type\\\":\\\"WhileDoType\\\",\\\"center_x\\\":500.0,\\\"center_y\\\":450.0,\\\"width\\\":400.0,\\\"height\\\":250.0,\\\"text\\\":\\\"\\\",\\\"false_knot\\\":{\\\"id\\\":\\\"1e9393b2-33f3-48f3-a160-3bbb275d6416\\\"},\\\"true_knot\\\":{\\\"id\\\":\\\"3ba52958-bc7a-4841-9e3a-69e7f3ee6b5d\\\"},\\\"top_knot\\\":{\\\"id\\\":\\\"b641ec1a-f91b-47fa-a70c-e49ffacc170b\\\"},\\\"bottom_knot\\\":{\\\"id\\\":\\\"b735ccdc-d884-41d3-a857-c3cce2feb76e\\\"},\\\"id\\\":\\\"73400d47-9b84-4f2c-95b6-fed6c04b4f36\\\"},{\\\"type\\\":\\\"CalculationType\\\",\\\"center_x\\\":525.0,\\\"center_y\\\":900.0,\\\"width\\\":400.0,\\\"height\\\":250.0,\\\"text\\\":\\\"\\\",\\\"left_knot\\\":{\\\"id\\\":\\\"f98f7506-be5e-4ab0-bd16-2c9ee85e8980\\\"},\\\"right_knot\\\":{\\\"id\\\":\\\"4a651521-5867-4a42-924a-ace743a666af\\\"},\\\"top_knot\\\":{\\\"id\\\":\\\"37000b0c-bdb0-4ad1-bb95-5214ddd00016\\\"},\\\"bottom_knot\\\":{\\\"id\\\":\\\"e5c8a3fc-79cb-454e-8ee5-0c4e42c368b1\\\"},\\\"id\\\":\\\"6f9422d3-81b4-4b47-8b5f-4e97c0e48e7e\\\"}],\\\"connections\\\":[{\\\"start_knot_id\\\":\\\"1e9393b2-33f3-48f3-a160-3bbb275d6416\\\",\\\"start_block_id\\\":\\\"73400d47-9b84-4f2c-95b6-fed6c04b4f36\\\",\\\"end_knot_id\\\":\\\"37000b0c-bdb0-4ad1-bb95-5214ddd00016\\\",\\\"end_block_id\\\":\\\"6f9422d3-81b4-4b47-8b5f-4e97c0e48e7e\\\"}]}\",\"task\":\"\"}", "Block Diagram", 1, 10),
                        Stage(3, "Coding", "Coding", "{\"testData\":[{\"input\":\"4\\n5\",\"output\":\"9\"}],\"code\":\"public class Main {\\n    public static void main(String[] args) {\\n        \\n    }\\n}\\n\",\"task\":\"<h1 style=\\\"text-align: center;\\\">Well...</h1><div>In this task please create a program that computing summ of two numbers. In input data come two integers and output is one integer.</div>\",\"language\":\"java\"}", "Code", 1, 10),
                        Stage(4, "Coding", "Coding", "", "Lecture", 1, 10),
                        Stage(5, "Coding", "Coding", "<h1 style=\"text-align: center;\">Wellcome!!!</h1><div><ul><li>Base python</li><li>Base syntaxis</li><li>Base math operations</li></ul><div><b>Very important teach the base information about python, because writing code on python and anyone</b><b style=\"\"><i>&nbsp;language is</i>sss</b></div></div>", "Lecture", 1, 10),
                        Stage(6, "Coding", "Coding", "", "Lecture", 1, 10),
                        Stage(7, "Coding", "Coding", "{\"testData\":[{\"input\":\"\",\"output\":\"\"}],\"diagramData\":\"{\\\"blocks\\\":[{\\\"type\\\":\\\"StartType\\\",\\\"center_x\\\":717.0,\\\"center_y\\\":373.0,\\\"width\\\":400.0,\\\"height\\\":250.0,\\\"text\\\":\\\"int x\\\",\\\"output_knot\\\":{\\\"id\\\":\\\"cebf9595-dc04-45f6-a1ea-fe834b52bc1f\\\"},\\\"id\\\":\\\"7fa5c97a-5a00-496f-bf1e-215c1d886c4d\\\"},{\\\"type\\\":\\\"CalculationType\\\",\\\"center_x\\\":828.0322,\\\"center_y\\\":851.9634,\\\"width\\\":400.0,\\\"height\\\":250.0,\\\"text\\\":\\\"\\\",\\\"left_knot\\\":{\\\"id\\\":\\\"e1e8aab7-ca33-4f39-86f4-248c18870991\\\"},\\\"right_knot\\\":{\\\"id\\\":\\\"78c7fe41-a16e-45af-96f8-602ff58b89ef\\\"},\\\"top_knot\\\":{\\\"id\\\":\\\"312db7e3-f3eb-4a4c-b71a-a74e48b9bef6\\\"},\\\"bottom_knot\\\":{\\\"id\\\":\\\"925f8119-32a7-44e4-bc7b-bee15c7cadb5\\\"},\\\"id\\\":\\\"282777d2-bde3-4913-bc59-260f3bb38130\\\"}],\\\"connections\\\":[{\\\"start_knot_id\\\":\\\"cebf9595-dc04-45f6-a1ea-fe834b52bc1f\\\",\\\"start_block_id\\\":\\\"7fa5c97a-5a00-496f-bf1e-215c1d886c4d\\\",\\\"end_knot_id\\\":\\\"312db7e3-f3eb-4a4c-b71a-a74e48b9bef6\\\",\\\"end_block_id\\\":\\\"282777d2-bde3-4913-bc59-260f3bb38130\\\"}]}\",\"task\":\"\"}", "Block Diagram", 1, 10),
                    )
                ),
                Module(2, "Second education", "Learning the basics", 1, 100,
                    mutableListOf(
                        Stage(8, "Coding", "Coding", "", "Lecture", 2, 10),
                        Stage(9, "Coding", "Coding", "", "Block Diagram", 2, 10),
                        Stage(10, "Coding", "Coding", "{\"testData\":[{\"input\":\"1 2\\n3\",\"output\":\"4\"},{\"input\":\"4\",\"output\":\"1\"}],\"code\":\"public class Main() {\\n    hello world\\n}\\n\",\"task\":\"Here napisana zadanie\",\"language\":\"python3\"}", "Code", 2, 10),
                        Stage(11, "Coding", "Coding", "", "Lecture", 2, 10),
                        Stage(12, "Coding", "Coding", "", "Lecture", 2, 10),
                        Stage(13, "Coding", "Coding", "", "Lecture", 2, 10),
                        Stage(14, "Coding", "Coding", "", "Block Diagram", 2, 10),
                    )
                ),
                Module(3, "Third education", "Learning the basics", 1, 100,
                    mutableListOf(
                        Stage(15, "Coding", "Coding", "", "Lecture", 3, 10),
                        Stage(16, "Coding", "Coding", "", "Block Diagram", 3, 10),
                        Stage(17, "Coding", "Coding", "", "Code", 3, 10),
                        Stage(18, "Coding", "Coding", "", "Lecture", 3, 10),
                        Stage(19, "Coding", "Coding", "", "Lecture", 3, 10),
                        Stage(20, "Coding", "Coding", "", "Lecture", 3, 10),
                        Stage(21, "Coding", "Coding", "", "Block Diagram", 3, 10),
                    )
                ),
                Module(4, "Fifth education", "Learning the basics", 1, 100,
                    mutableListOf(
                        Stage(22, "Coding", "Coding", "", "Lecture", 4, 10),
                        Stage(23, "Coding", "Coding", "", "Block Diagram", 4, 10),
                        Stage(24, "Coding", "Coding", "", "Code", 4, 10),
                        Stage(25, "Coding", "Coding", "", "Lecture", 4, 10),
                        Stage(26, "Coding", "Coding", "", "Lecture", 4, 10),
                        Stage(27, "Coding", "Coding", "", "Lecture", 4, 10),
                        Stage(28, "Coding", "Coding", "", "Block Diagram", 4, 10),
                    )
                )
            )
        )
    }

    override suspend fun updateModule(module: Module): Result<Unit> {
        delay(5000)
        return Result.success(Unit)
    }

    override suspend fun updateStage(stage: Stage): Result<Unit> {
        delay(5000)
        return Result.success(Unit)
    }

    override suspend fun deleteModule(moduleId: Int): Result<Unit> {
        delay(5000)
        return Result.success(Unit)
    }

    override suspend fun deleteStage(stageId: Int): Result<Unit> {
        delay(5000)
        return Result.success(Unit)
    }

    override suspend fun getTakesModules(courseId: Int): Result<List<TakesModule>> {
        delay(5000)
        return Result.success(
            listOf(
                TakesModule(1, 100,"Start education", "Learning the basics", 1, 100,
                    mutableListOf(
                        TakesStage(1, "Coding", "Coding", "<h1 style=\"text-align: center;\">Wellcome!!!</h1><div><ul><li>Base python</li><li>Base syntaxis</li><li>Base math operations</li></ul><div><b>Very important teach the base information about python, because writing code on python and anyone</b><b style=\"\"><i>&nbsp;language is</i>sss</b></div></div><h1 style=\"text-align: center;\">Wellcome!!!</h1><div><ul><li>Base python</li><li>Base syntaxis</li><li>Base math operations</li></ul><div><b>Very important teach the base information about python, because writing code on python and anyone</b><b style=\"\"><i>&nbsp;language is</i>sss</b></div></div><h1 style=\"text-align: center;\">Wellcome!!!</h1><div><ul><li>Base python</li><li>Base syntaxis</li><li>Base math operations</li></ul><div><b>Very important teach the base information about python, because writing code on python and anyone</b><b style=\"\"><i>&nbsp;language is</i>sss</b></div></div><h1 style=\"text-align: center;\">Wellcome!!!</h1><div><ul><li>Base python</li><li>Base syntaxis</li><li>Base math operations</li></ul><div><b>Very important teach the base information about python, because writing code on python and anyone</b><b style=\"\"><i>&nbsp;language is</i>sss</b></div></div><h1 style=\"text-align: center;\">Wellcome!!!</h1><div><ul><li>Base python</li><li>Base syntaxis</li><li>Base math operations</li></ul><div><b>Very important teach the base information about python, because writing code on python and anyone</b><b style=\"\"><i>&nbsp;language is</i>sss</b></div></div><h1 style=\"text-align: center;\">Wellcome!!!</h1><div><ul><li>Base python</li><li>Base syntaxis</li><li>Base math operations</li></ul><div><b>Very important teach the base information about python, because writing code on python and anyone</b><b style=\"\"><i>&nbsp;language is</i>sss</b></div></div><h1 style=\"text-align: center;\">Wellcome!!!</h1><div><ul><li>Base python</li><li>Base syntaxis</li><li>Base math operations</li></ul><div><b>Very important teach the base information about python, because writing code on python and anyone</b><b style=\"\"><i>&nbsp;language is</i>sss</b></div></div><h1 style=\"text-align: center;\">Wellcome!!!</h1><div><ul><li>Base python</li><li>Base syntaxis</li><li>Base math operations</li></ul><div><b>Very important teach the base information about python, because writing code on python and anyone</b><b style=\"\"><i>&nbsp;language is</i>sss</b></div></div><h1 style=\"text-align: center;\">Wellcome!!!</h1><div><ul><li>Base python</li><li>Base syntaxis</li><li>Base math operations</li></ul><div><b>Very important teach the base information about python, because writing code on python and anyone</b><b style=\"\"><i>&nbsp;language is</i>sss</b></div></div><h1 style=\"text-align: center;\">Wellcome!!!</h1><div><ul><li>Base python</li><li>Base syntaxis</li><li>Base math operations</li></ul><div><b>Very important teach the base information about python, because writing code on python and anyone</b><b style=\"\"><i>&nbsp;language is</i>sss</b></div></div><h1 style=\"text-align: center;\">Wellcome!!!</h1><div><ul><li>Base python</li><li>Base syntaxis</li><li>Base math operations</li></ul><div><b>Very important teach the base information about python, because writing code on python and anyone</b><b style=\"\"><i>&nbsp;language is</i>sss</b></div></div><h1 style=\"text-align: center;\">Wellcome!!!</h1><div><ul><li>Base python</li><li>Base syntaxis</li><li>Base math operations</li></ul><div><b>Very important teach the base information about python, because writing code on python and anyone</b><b style=\"\"><i>&nbsp;language is</i>sss</b></div></div><h1 style=\"text-align: center;\">Wellcome!!!</h1><div><ul><li>Base python</li><li>Base syntaxis</li><li>Base math operations</li></ul><div><b>Very important teach the base information about python, because writing code on python and anyone</b><b style=\"\"><i>&nbsp;language is</i>sss</b></div></div><h1 style=\"text-align: center;\">Wellcome!!!</h1><div><ul><li>Base python</li><li>Base syntaxis</li><li>Base math operations</li></ul><div><b>Very important teach the base information about python, because writing code on python and anyone</b><b style=\"\"><i>&nbsp;language is</i>sss</b></div></div><h1 style=\"text-align: center;\">Wellcome!!!</h1><div><ul><li>Base python</li><li>Base syntaxis</li><li>Base math operations</li></ul><div><b>Very important teach the base information about python, because writing code on python and anyone</b><b style=\"\"><i>&nbsp;language is</i>sss</b></div></div><h1 style=\"text-align: center;\">Wellcome!!!</h1><div><ul><li>Base python</li><li>Base syntaxis</li><li>Base math operations</li></ul><div><b>Very important teach the base information about python, because writing code on python and anyone</b><b style=\"\"><i>&nbsp;language is</i>sss</b></div></div>", "Lecture", 1, 100, 10),
                        TakesStage(2, "Coding", "Coding", "{\"testData\":[{\"input\":\"4\",\"output\":\"9\"}],\"diagramData\":\"{\\\"blocks\\\":[{\\\"type\\\":\\\"StartType\\\",\\\"center_x\\\":717.0,\\\"center_y\\\":373.0,\\\"width\\\":400.0,\\\"height\\\":250.0,\\\"text\\\":\\\"int x\\\",\\\"output_knot\\\":{\\\"id\\\":\\\"cebf9595-dc04-45f6-a1ea-fe834b52bc1f\\\"},\\\"id\\\":\\\"7fa5c97a-5a00-496f-bf1e-215c1d886c4d\\\"},{\\\"type\\\":\\\"CalculationType\\\",\\\"center_x\\\":828.0322,\\\"center_y\\\":851.9634,\\\"width\\\":400.0,\\\"height\\\":250.0,\\\"text\\\":\\\"\\\",\\\"left_knot\\\":{\\\"id\\\":\\\"e1e8aab7-ca33-4f39-86f4-248c18870991\\\"},\\\"right_knot\\\":{\\\"id\\\":\\\"78c7fe41-a16e-45af-96f8-602ff58b89ef\\\"},\\\"top_knot\\\":{\\\"id\\\":\\\"312db7e3-f3eb-4a4c-b71a-a74e48b9bef6\\\"},\\\"bottom_knot\\\":{\\\"id\\\":\\\"925f8119-32a7-44e4-bc7b-bee15c7cadb5\\\"},\\\"id\\\":\\\"282777d2-bde3-4913-bc59-260f3bb38130\\\"}],\\\"connections\\\":[{\\\"start_knot_id\\\":\\\"cebf9595-dc04-45f6-a1ea-fe834b52bc1f\\\",\\\"start_block_id\\\":\\\"7fa5c97a-5a00-496f-bf1e-215c1d886c4d\\\",\\\"end_knot_id\\\":\\\"312db7e3-f3eb-4a4c-b71a-a74e48b9bef6\\\",\\\"end_block_id\\\":\\\"282777d2-bde3-4913-bc59-260f3bb38130\\\"}]}\",\"task\":\"\"}", "BlockDiagram", 1, 100, 10, true),
                        TakesStage(3, "Coding", "Coding", "{\"testData\":[{\"input\":\"5 2 7 10 2 1 8 3\n8\",\"output\":\"5\"}],\"task\":\"&nbsp;Tut zadanie o vsiakom raznon\",\"diagramData\":\"\"}", "BlockDiagram", 1, 100, 10),
                        TakesStage(4, "Coding", "Coding", "{\"testData\":[{\"input\":\"4\\n5\",\"output\":\"9\"}],\"code\":\"import java.util.Scanner;\\npublic class Main {    \\n    public static void main(String[] args) {\\n        Scanner in = new Scanner(System.in);\\n        int first = in.nextInt();\\n        int second = in.nextInt();\\n        int sum = first + second;\\n        System.out.printf(\\\"%d\\\", sum);\\n    }\\n}\\n\",\"task\":\"<h1 style=\\\"text-align: center;\\\">Well...</h1><div>In this task please create a program that computing summ of two numbers. In input data come two integers and output is one integer.</div>\",\"language\":\"java\"}", "Code", 1, 100, 10),
                        TakesStage(5, "Coding", "Coding", "<h1 style=\"text-align: center;\">Wellcome!!!</h1><div><ul><li>Base python</li><li>Base syntaxis</li><li>Base math operations</li></ul><div><b>Very important teach the base information about python, because writing code on python and anyone</b><b style=\"\"><i>&nbsp;language is</i>sss</b></div></div>", "Lecture", 1, 100, 10),
                        TakesStage(6, "Coding", "Coding", "<h1 style=\"text-align: center;\">Wellcome!!!</h1><div><ul><li>Base python</li><li>Base syntaxis</li><li>Base math operations</li></ul><div><b>Very important teach the base information about python, because writing code on python and anyone</b><b style=\"\"><i>&nbsp;language is</i>sss</b></div></div>", "Lecture", 1, 100, 10),
                        TakesStage(7, "Coding", "Coding", "<h1 style=\"text-align: center;\">Wellcome!!!</h1><div><ul><li>Base python</li><li>Base syntaxis</li><li>Base math operations</li></ul><div><b>Very important teach the base information about python, because writing code on python and anyone</b><b style=\"\"><i>&nbsp;language is</i>sss</b></div></div>", "Lecture", 1, 100, 10),
                        TakesStage(8, "Coding", "Coding", "{\"testData\":[{\"input\":\"4 5\\n1\",\"output\":\"5\"},{\"input\":\"6\",\"output\":\"3\"}],\"task\":\"&nbsp;Tut zadanie o vsiakom raznon\",\"diagramData\":\"\"}", "BlockDiagram", 1, 100, 10),
                        TakesStage(9, "Coding", "Coding", "<h1 style=\"text-align: center;\">Wellcome!!!</h1><div><ul><li>Base python</li><li>Base syntaxis</li><li>Base math operations</li></ul><div><b>Very important teach the base information about python, because writing code on python and anyone</b><b style=\"\"><i>&nbsp;language is</i>sss</b></div></div>", "Lecture", 1, 100, 10),
                    )
                ),
                TakesModule(2, 100,"Continue education", "Learning the basics", 1, 100,
                    mutableListOf(
                        TakesStage(10, "Coding", "Coding", "<h1 style=\"text-align: center;\">Wellcome!!!</h1><div><ul><li>Base python</li><li>Base syntaxis</li><li>Base math operations</li></ul><div><b>Very important teach the base information about python, because writing code on python and anyone</b><b style=\"\"><i>&nbsp;language is</i>sss</b></div></div>", "Lecture", 1, 100, 10),
                        TakesStage(11, "Coding", "Coding", "{\"testData\":[{\"input\":\"4 5\\n1\",\"output\":\"5\"}],\"task\":\"&nbsp;Tut zadanie o vsiakom raznon\",\"diagramData\":\"\"}", "BlockDiagram", 1, 100, 10),
                        TakesStage(12, "Coding", "Coding", "{\"testData\":[{\"input\":\"1 2\\n3\",\"output\":\"4\"},{\"input\":\"4\",\"output\":\"1\"}],\"code\":\"public class Main() {\\n    hello world\\n}\\n\",\"task\":\"Here napisana zadanie\",\"language\":\"java\"}", "Code", 1, 100, 10),
                        TakesStage(13, "Coding", "Coding", "", "Lecture", 1, 100, 10),
                        TakesStage(14, "Coding", "Coding", "", "Lecture", 1, 100, 10),
                        TakesStage(15, "Coding", "Coding", "<h1 style=\"text-align: center;\">Wellcome!!!</h1><div><ul><li>Base python</li><li>Base syntaxis</li><li>Base math operations</li></ul><div><b>Very important teach the base information about python, because writing code on python and anyone</b><b style=\"\"><i>&nbsp;language is</i>sss</b></div></div>", "Lecture", 1, 100, 10),
                        TakesStage(16, "Coding", "Coding", "{\"testData\":[{\"input\":\"4 5\\n1\",\"output\":\"5\"}],\"task\":\"&nbsp;Tut zadanie o vsiakom raznon\",\"diagramData\":\"\"}", "BlockDiagram", 1, 100, 10),
                        TakesStage(17, "Coding", "Coding", "<h1 style=\"text-align: center;\">Wellcome!!!</h1><div><ul><li>Base python</li><li>Base syntaxis</li><li>Base math operations</li></ul><div><b>Very important teach the base information about python, because writing code on python and anyone</b><b style=\"\"><i>&nbsp;language is</i>sss</b></div></div>", "Lecture", 1, 100, 10),
                    )
                ),
                TakesModule(3, 100,"More hard", "Learning the basics", 1, 100,
                    mutableListOf(
                        TakesStage(18, "Coding", "Coding", "", "Lecture", 1, 100, 10),
                        TakesStage(19, "Coding", "Coding", "{\"testData\":[{\"input\":\"4 5\\n1\",\"output\":\"5\"}],\"task\":\"&nbsp;Tut zadanie o vsiakom raznon\",\"diagramData\":\"\"}", "BlockDiagram", 1, 100, 10),
                        TakesStage(20, "Coding", "Coding", "", "Code", 1, 100, 10),
                        TakesStage(21, "Coding", "Coding", "", "Lecture", 1, 100, 10),
                        TakesStage(22, "Coding", "Coding", "", "Lecture", 1, 100, 10),
                        TakesStage(23, "Coding", "Coding", "", "Lecture", 1, 100, 10),
                        TakesStage(24, "Coding", "Coding", "{\"testData\":[{\"input\":\"4 5\\n1\",\"output\":\"5\"}],\"task\":\"&nbsp;Tut zadanie o vsiakom raznon\",\"diagramData\":\"\"}", "BlockDiagram", 1, 100, 10),
                        TakesStage(25, "Coding", "Coding", "<h1 style=\"text-align: center;\">Wellcome!!!</h1><div><ul><li>Base python</li><li>Base syntaxis</li><li>Base math operations</li></ul><div><b>Very important teach the base information about python, because writing code on python and anyone</b><b style=\"\"><i>&nbsp;language is</i>sss</b></div></div>", "Lecture", 1, 100, 10),
                    )
                ),
                TakesModule(4, 100,"Very important", "Learning the basics", 1, 100,
                    mutableListOf(
                        TakesStage(26, "Coding", "Coding", "", "Lecture", 1, 100, 10),
                        TakesStage(27, "Coding", "Coding", "{\"testData\":[{\"input\":\"4 5\\n1\",\"output\":\"5\"}],\"task\":\"&nbsp;Tut zadanie o vsiakom raznon\",\"diagramData\":\"\"}", "BlockDiagram", 1, 100, 10),
                        TakesStage(28, "Coding", "Coding", "", "Code", 1, 100, 10),
                        TakesStage(29, "Coding", "Coding", "", "Lecture", 1, 100, 10),
                        TakesStage(30, "Coding", "Coding", "", "Lecture", 1, 100, 10),
                        TakesStage(31, "Coding", "Coding", "", "Lecture", 1, 100, 10),
                        TakesStage(32, "Coding", "Coding", "{\"testData\":[{\"input\":\"4 5\\n1\",\"output\":\"5\"}],\"task\":\"&nbsp;Tut zadanie o vsiakom raznon\",\"diagramData\":\"\"}", "BlockDiagram", 1, 100, 10),
                        TakesStage(33, "Coding", "Coding", "<h1 style=\"text-align: center;\">Wellcome!!!</h1><div><ul><li>Base python</li><li>Base syntaxis</li><li>Base math operations</li></ul><div><b>Very important teach the base information about python, because writing code on python and anyone</b><b style=\"\"><i>&nbsp;language is</i>sss</b></div></div>", "Lecture", 1, 100, 10),
                    )
                ),
            )
        )
    }

    override suspend fun updateTakesStage(takesStage: TakesStage): Result<Unit> {
        delay(5000)
        return Result.success(Unit)
    }

    override suspend fun subscribe(courseId: Int): Result<Unit> {
        delay(5000)
        return Result.success(Unit)
    }

    override suspend fun unsubscribe(courseId: Int): Result<Unit> {
        delay(5000)
        return Result.success(Unit)
    }
}