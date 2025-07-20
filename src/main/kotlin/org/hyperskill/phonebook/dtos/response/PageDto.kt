package org.hyperskill.phonebook.dtos.response

import org.springframework.data.domain.Page

data class PageDto<T>(
    val content: List<T>,
    val totalElements: Long,
    val totalPages: Int,
    val currentPage: Int,
    val size: Int
) {
    companion object {
        fun <T, R> fromPage(page: Page<T>, converter: (T) -> R): PageDto<R> {
            return PageDto(
                content = page.content.map(converter),
                totalElements = page.totalElements,
                totalPages = page.totalPages,
                currentPage = page.number,
                size = page.size
            )
        }
    }
}
