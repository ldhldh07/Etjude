<template>
  <div class="all-film-list">
    <div class="film-list">
      <FilmListItem
        v-for="film in allFilmList"
        :key="film.id"
        :film="film"
        :createdDate="film.createdDate"
        :likeCount="film.articleLikeCount"
      >
      </FilmListItem>
    </div>
    <div class="film-list__pagination-section">
      <v-pagination
        v-model="page"
        :pages="pageCount"
        :range-size="1"
        active-color="#ffeff2"
        @update:modelValue="updateHandler"
        class="film-list__pagination"
        @click="updatePage"
      />
    </div>
  </div>
</template>
<script>
import { ref, computed } from "vue";
import FilmListItem from "@/components/film/FilmListItem.vue";
import dummyfilms from "@/dummy/filmdummydata/page1.json";
import VPagination from "@hennge/vue3-pagination";
import "@hennge/vue3-pagination/dist/vue3-pagination.css";
import { getFilmPage } from "@/api/films";

export default {
  name: "FilmList",
  components: {
    FilmListItem,
    VPagination,
  },
  setup() {
    const page = ref(1);
    const allFilmList = ref(null);
    const getFilmPageList = () => {
      getFilmPage(
        page.value,
        ({ data }) => {
          allFilmList.value = data;
        },
        (error) => {
          console.log("영화 페이지 에러:", error);
          allFilmList.value = null;
        }
      );
    };
    getFilmPageList();
    const updatePage = () => {
      getFilmPageList();
    };
    const pageCount = computed(() => {
      if (allFilmList.value) {
        return Math.ceil(allFilmList.value[0].totalArticleNumber / 12);
      }
      return pageCount.value;
    });
    return {
      page,
      allFilmList,
      dummyfilms,
      updatePage,
      pageCount,
    };
  },
};
</script>

<style scoped lang="scss">
.all-film-list {
  display: flex;
  flex-direction: column;
}
.film-list {
  display: flex;
  margin: 0px 60px;
  flex-wrap: wrap;
}
.film-list__pagination-section {
  display: flex;
  flex-direction: column;
  align-items: center;
}
.film-list__pagination {
  margin: 20px 0px;
}
</style>
