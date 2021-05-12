

<template>
  <div
    class="fixed z-10 inset-0 modal h-full flex justify-center"
    aria-labelledby="modal-title"
    role="dialog"
    aria-modal="true"
  >
    <div class="container max-w-sm mx-auto flex items-center">
      <div
        class="fixed inset-0 bg-gray-500 bg-opacity-75 transition-opacity"
        aria-hidden="true"
      />
      <div class="bg-white rounded-lg p-6 shadow-xl transform w-full">
        <font-awesome-icon
          icon="times-circle"
          class="text-2xl cursor-pointer block m-auto mr-0"
          @click="$emit('close')"
        />
        <div class="mt-3 sm:mt-5">
          <h3
            id="modal-title"
            class="text-lg leading-6 font-medium text-gray-900"
          >
            {{ $t("game.createNewRoom") }}
          </h3>
          <div class="mt-2">
            <form class="mt-5 sm:mt-0" @submit.prevent="handleSubmit">
              <div class="form-group">
                <label class="block text-gray-700 text-sm mb-2" for="topic">{{
                  $t("game.roomName")
                }}</label>
                <div
                  v-show="submitted && $v.form.roomName.$error"
                  class="has-errors py-1 text-xs"
                >
                  {{ $t("errors.validation.nameRequired") }}
                </div>
                <input
                  id="roomName"
                  v-model="form.roomName"
                  type="text"
                  name="room-name"
                  class="form-control block border border-grey-light w-full p-3 rounded mb-4"
                  :class="{
                    'border-red-500 !important':
                      submitted && $v.form.roomName.$error,
                  }"
                  :placeholder="$t('game.roomName')"
                />
              </div>
              <div class="form-group">
                <label class="block text-gray-700 text-sm mb-2" for="topic">{{
                  $t("game.topic")
                }}</label>
                <div
                  v-show="submitted && $v.form.topic.$error"
                  class="has-errors py-1 text-xs"
                >
                  {{ $t("errors.validation.topicRequired") }}
                </div>
                <multiselect
                  v-model="selected"
                  track-by="id"
                  label="name"
                  select-label=""
                  deselect-label=""
                  :placeholder="$t('game.selectTopic')"
                  :options="topicList"
                  :searchable="true"
                  :allow-empty="false"
                  :class="{
                    'border rounded border-red-500':
                      submitted && $v.form.topic.$error,
                  }"
                />
              </div>
              <div class="form-group mt-5 sm:mt-6">
                <button
                  type="submit"
                  class="w-full inline-flex justify-center rounded bg-gray-900 text-white hover:bg-gray-700 px-4 py-2 focus:ring-2 focus:ring-offset-2 focus:outline-none focus:ring-indigo-500"
                >
                  {{ $t("game.createRoom") }}
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
</template> <script>
import { mapGetters, mapActions } from "vuex";
import { required } from "vuelidate/lib/validators";
export default {
  name: "TopicForm",
  data() {
    return {
      submitted: false,
      selected: "",
      form: {
        topic: "",
      },
    };
  },
  computed: { ...mapGetters(["topicList"]) },
  watch: {
    selected(newValue) {
      this.form.topic = newValue.id;
    },
  },
  created() {
    this.fetchTopics();
  },
  validations: { form: { roomName: { required }, topic: { required } } },
  methods: {
    ...mapActions(["fetchTopics", "createRoom"]),
    handleSubmit() {
      this.submitted = true;
      this.$v.$touch();
      if (this.$v.$invalid) {
        return;
      }
      this.createRoom(this.form)
        .then(() => {
          this.$notify({
            title: this.$t("game.messages.roomCreateSuccess"),
            type: "success",
          });
          this.$emit("close");
        })
        .catch((err) => {
          console.error(err);
        });
    },
  },
};

